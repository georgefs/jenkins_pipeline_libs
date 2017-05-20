def install(){
    sh """ 
	if ! [ -x "$(command -v aws)" ]; then
		sudo apt-get update && sudo apt-get install awscli
	fi
    """
}


def login(){
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'aws', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
        sh "aws configure set aws_access_key_id ${USERNAME}"
        sh "aws configure set aws_secret_access_key ${PASSWORD}"
    }
}
