def call(body){
    def host = "172.17.42.1"
    def user = "root"
    def password = "test"
    def container = sh(returnStdout: true, script: "docker run -d --rm -p 5432 -e POSTGRES_USER=${user} -e POSTGRES_PASSWORD=${password} postgres").trim()
    def port = sh(returnStdout=true, script: "docker port ${container} 5432|sed 's/0.0.0.0://'").trim()
    withEnv(["PGHOST=${host}", "PGUSER=${user}", "PGPASSWORD=${password}", "PGPORT=${port}"]){
        body()
    }
    sh "docker rm -f ${container}"
}
