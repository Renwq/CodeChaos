package com.rwq.plugins.services

import com.intellij.openapi.project.Project
import com.rwq.plugins.MyBundle

class MyProjectService(project: Project) {


    init {
        println(MyBundle.message("projectService", project.name))

        println(MyBundle.message("name", "CodeChaos"))

        staticProject = project


    }

    companion object {
        public var staticProject: Project? = null;
    }
}
