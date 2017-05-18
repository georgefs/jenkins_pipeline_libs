def call(body){
    
    body.use_cert = false
    body.test_script = {}
    body.release = {}
    body()
    Closure test_script = body.test_script
    Closure release = body.release
    use_cert = body.use_cert
    
    hash = sh(returnStdout=true, script='git rev-parse --short HEAD')

    ansiColor('xterm') {
        stage('prepare'){
            checkout scm
                withCredentials([file(credentialsId: 'certfile', variable: 'certfile')]) {
                    if(use_cert){
                        sh 'mv $certfile certfile.tgz'
                            sh 'tar zxvf certfile.tgz'
                    }
                }
        }

        stage('build'){
            sh "docker build . -t ${hash}"
        }

        stage('test'){
            withDockerContainer(image:body.name, args: '-u root:root') {
                test_script()
            }
        }

        stage('release'){
            sh "docker tag ${hash} ${body.name}"
            if(is_release()){
                release()
            }
            sh "docker rmi ${body.name}"
        }
    }
}


// check current is release
def is_release(){
    version = release_version()
    if(version){
		return true
    }else{
        return false
    }
}

// get release version from git commit
def release_version(){
    def commit = github.current_commit_message()
    if(commit ==~ /release-v[\d.]+/ ){
		def version = (commit =~ /release-(v[\d.]+)/)[0][1]
        return version
    }else{
        return false

    }
}
