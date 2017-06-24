def call(body){
    
    body.use_cert = false
    body.test_script = {}
    body.release = {}
    body()
    Closure test_script = body.test_script
    Closure release = body.release
    use_cert = body.use_cert

    body.image_id = image_tag()

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
            sh(returnStdout:true, script:"docker build . -t ${body.image_id}")
        }

        stage('test'){
            withDockerContainer(image:body.image_id, args: '-u root:root') {
                test_script()
                sh 'git clean -fd'
            }
        }

        stage('release'){
            sh "docker tag ${body.image_id} ${body.name}"
            if(is_release()){
                release()
            }
            try{
                sh "docker rmi ${body.image_id}"
            } catch(e) {
                echo "remove ${body.image_id} error" 
            }

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

def image_tag(){
    return BUILD_TAG.replace("%2F", "_").toLowerCase()
}
