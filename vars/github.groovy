/*
    序
        github jenkins plugin

    設定
        github_token: Credential Secret text
        github: username & password for github

*/
def current_commit_message(){
    def GIT_COMMENT = sh(returnStdout: true, script: 'git log --pretty=oneline -1|sed \'s/[^ ]* //\'').trim()
    return GIT_COMMENT
}

def current_repo(repo='origin'){
    def GIT_REPO = sh(returnStdout: true, script: "git remote get-url ${repo}|sed \'s/https:\\/\\///\'|sed \'s/git@//\'|sed \"s/:/\\//\"").trim()
    return GIT_REPO
}

def push_tag(tag, create=true){
    if(create){
        sh "git tag ${tag}"
    }
    def repo = current_repo().replaceFirst(/^git@/, '').replaceFirst(/https?:\/\//, '').replace(/github.com:/, "github.com/")

    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'github', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
        sh "git push https://${GIT_USERNAME}:${GIT_PASSWORD}@${repo} ${tag}"
    }
}
