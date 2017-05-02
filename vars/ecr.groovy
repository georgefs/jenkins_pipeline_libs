/*
    序
        amazon ecr jenkins plugin

    設定
        ECR_DOMAIN: amazon ecr name
        ECR_KEY: amazon ecs key
*/

def push_image(name, version='latest', origin_name=null){
    origin_name = origin_name || name
    def real_name = '${ECR_DOMAIN}/${name}:${version}'
    sh 'docker tag ${origin_name} ${real_name}'
    docker.withRegistry("https://${ECR_DOMAIN}", "${ECR_KEY}") {
        sh 'docker push ${real_name}' 
    }
}

def pull_image(name, version='latest', target_name=null){
    target_name = target_name || name
    def real_name = '${ECR_DOMAIN}/${name}:${version}'
    docker.withRegistry("https://${ECR_DOMAIN}", "${ECR_KEY}") {
        sh 'docker pull ${real_name}'
        sh 'docker tag ${real_name} ${name}'
    }
    return name
}
