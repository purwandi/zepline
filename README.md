## Required Plugins

- https://plugins.jenkins.io/gitlab-plugin/
- https://plugins.jenkins.io/pipeline-utility-steps/
- https://plugins.jenkins.io/docker-workflow/
- https://plugins.jenkins.io/docker-plugin/

## Pros

- Jenkinsless
- Parsing yaml file menjadi jenkins jobs
- Job template support via yaml
- Retrieve credentials
- Docker dan Server Native support

## Const

- Butuh docker agent apabila terdapat jobs yang memerlukan container


## Example

```
# image: alpine:latest

docker:
  - registry: https://ghcr.io
    credential: github-credentials
  - registry: https://index.docker.io/v1/
    credential: dockerhub-credentials

stages:
  - prepare
  - build
  - release
  - post

include:
  - remote: 'https://github.com/purwandi/zepline-test.git'
    credential: github-credentials
    ref: main
    files: 
      - /helm.yaml

variables:
  DOCKER_REGISTRY: ghcr.io
  # DOCKER_TLS_CERTDIR: $WORKSPACE/.certs
  # DOCKER_TLS_VERIFY: true
  # DOCKER_DRIVER: overlay2 
  # DOCKER_HOST: "tcp://docker:2376"

tasks:
  release:
    # image: alpine:3.13
    stage: release
    script: 
      - echo "hello from alpine 3.13"
      - sleep 10

  notification:
    # image: alpine
    stage: prepare
    script:
      - ls -all
      - env
      - echo "hello notification"
      - sleep 15

  docker:
    # image: docker
    stage: build
    credentials:
      - credential: github-credentials
        type: usernamePassword
        variables:
          username: GITHUB_USERNAME
          palssword: GITHUB_PASSWORD
    script:
      - ls -all
      - env
      - echo $GITHUB_USERNAME
      - echo $GITHUB_PASSWORD

  helm-cannary:
    image: alpine
    stage: build
    extends: .helm

  helm-uat:
    image: alpine
    stage: build
    extends: .helm
    only: 
      - master

  helm-prod:
    image: alpine
    stage: build
    extends: .helm
    only: 
      - tags

  helm:
    image: alpine
    extends: .helm
    credentials:
      - credential: github-credentials
        type: usernamePassword
        variables:
          username: GITHUB_USERNAME
          password: GITHUB_PASSWORD
      # - credential: github-credentials
      #   type: usernameColonPassword
      #   variables:
      #     variable: GITHUB_TOKEN
      # - credential: file-secret
      #   type: file
      #   variables:
      #     variable: FILE_SECRET
      # - credential: secret-text
      #   type: string
      #   variables:
      #     variable: FILE_SECRET
    stage: build
    # services:
    #   - image: mysql:alpine
    #     alias: db
    script:
      - echo $GITHUB_USERNAME
      - echo $GITHUB_PASSWORD
      - sleep 10
    only:
      - master
      - develop
    when:
      - manual

  # helm:
  #   extends: .helm
  #   stage: release
  #   variables:
  #     ENV: prod
  ```