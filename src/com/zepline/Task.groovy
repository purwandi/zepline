package com.zepline

class Task {
  String        name
  Config        config
  def           script

  Task (String name, def config, def script) {
    this.name     = name
    this.config   = config
    this.script   = script
  }

  def execute() {
    
  }

  // def run() {
    
    // script.sh 'docker network create bridge1 || true'
    // script.sh(script: 'docker run --net bridge1 --name nginx -d nginx:alpine', returnStdout: true)
    // script.sh(script: 'docker run --net bridge1 --name httpd -d httpd:2.4-alpine', returnStdout: true)

    // script.docker.image('alpine:latest').inside('--net bridge1') { c ->
    //   // script


    //   // script.stage('test nginx') {
    //   //   script.sh 'curl http://nginx'
    //   // }
      
    //   // script.stage('test httpd') {
    //   //   script.sh 'curl http://httpd'
    //   // }
    // }

    // script.sh 'docker rm nginx httpd --force'
    // script.sh 'docker network rm bridge1 --force'
  // }
}