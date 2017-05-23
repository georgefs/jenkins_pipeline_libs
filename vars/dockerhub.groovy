def push_image(name, version='latest', origin_name=null){
    if(!origin_name){
        origin_name = name
    }
    sh 'env'   
    println origin_name
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockerhub', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASSWORD']]) {
        sh 'env'
        def real_name = "${DOCKERHUB_USER}/${name}:${version}"
        sh "docker login -u ${DOCKERHUB_USER} -p ${DOCKERHUB_PASSWORD}"
        sh "docker tag ${name} ${real_name}"
        sh "docker push ${real_name}"
        sh "docker logout"
    }
}

def pull_image( name, version='latest', target_name=null){ 
    if(!target_name){
        target_name = name
    }
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockerhub', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASSWORD']]) {
        def real_name = "${DOCKERHUB_USER}/${name}:${version}"
        sh 'env'
        sh "echo ${DOCKERHUB_USER}"
        sh "echo ${DOCKERHUB_PASSWORD}"
        sh "docker login -u ${DOCKERHUB_USER} -p ${DOCKERHUB_PASSWORD}"
        sh "docker pull ${real_name}"
        sh "docker tag ${real_name} ${name}"
        sh "docker logout"
    }
}
