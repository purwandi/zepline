package com.zepline

class Task {
  String        name
  Config        config

  Task (String name, Config config) {
    this.name     = name
    this.config   = config
  }

  def execute(def script) {
    String links = ""
    String containerIds = ""

    if (config.script == null) {
      throw new Exception("The script property in '${k}' is not defined ")
    }

    def task = {
      if (config.services) {
        if (config.image == null) {
          throw new Exception("Sidecar service only available in docker pipeline")
        }

        config.services.each { service ->
          // service callback
          def svc = {
            def image = script.docker.image(service.image)
            image.pull()

            def ctr  = image.run("-v ${script.env.WORKSPACE}:${script.env.WORKSPACE}")

            links = links +  " --link $ctr.id:${service.alias}"
            ctrIds = " $ctr.id "
          }

          WithImageRegistry.parse(service, config.docker, script, svc)
        }
      }

      def cmd = { cmds ->
        if (cmds == null) {
          return
        }

        cmds.each { command -> 
          Command.parse(script, command)
        }
      }

      // if image is defined we run it using docker
      if (config.image) {
        // service callback
        def svc = {
          def image = script.docker.image(config.image)
          image.pull()
          image.inside("$links") { 
            cmd(config.before_script)
            cmd(config.script)
            cmd(config.after_script)
          }
        }

        WithImageRegistry.parse(config, config.docker, script, svc)
      } else {
        cmd(config.before_script)
        cmd(config.script)
        cmd(config.after_script)
      }
    }

    try {
      WithEnvironment.parse(config, script)

      if (config.credentials) {
        // script.sh "echo 'using credentials'"
        task = WithCredentials.parse(config.credentials, script, task)
      }

      task()
    } finally {
      if (config.services) {
        Command.parse(script, "docker rm $containerIds --force")
      }
    }
  }
}
