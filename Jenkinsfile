// @Library('zepline-ci') _
library identifier: 'zepline-ci@feature/git-env', retriever: modernSCM([
  $class: 'GitSCMSource',
  // credentialsId: 'your-credentials-id',
  remote: 'https://gitlab.com/purwandi/zepline.git'
])

node {
  checkout scm

  zepline()
}
