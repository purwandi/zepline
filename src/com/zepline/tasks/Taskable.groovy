package com.zepline.tasks

abstract class Taskable {
  protected def script
  String name

  def Taskable(String name) {
    this.name = name
  }

  def execute() {
    return this.run()
  }

  abstract protected run()

}
