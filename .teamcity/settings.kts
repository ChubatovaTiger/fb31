import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.matrix
import jetbrains.buildServer.configs.kotlin.buildSteps.script

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2023.11"

project {

    buildType(Build2)
    buildType(Build1)
    buildType(Build3)
}

object Build1 : BuildType({
    name = "build1"

    //type = BuildTypeSettings.Type.COMPOSITE

    vcs {
        root(DslContext.settingsRoot)

        showDependenciesChanges = true
    }

    dependencies {
        snapshot(Build2) {
            reuseBuilds = ReuseBuilds.NO
        }
    }
    features {
        matrix {
            param("ex", listOf(
                value("1"),
                value("2")
            ))
        }
    }
    requirements {
        contains("teamcity.agent.name", "aaaaa")
    }
  failureConditions {
        executionTimeoutMin = 1
    }
    steps {
        script {
            id = "simpleRunner"
            scriptContent = "sleep 120"
        }
    }
})

object Build2 : BuildType({
    name = "build2"
})

object Build3 : BuildType({
    name = "build3"

    features {
        matrix {
            param("ex", listOf(
                value("1"),
                value("2")
            ))
        }
    }
/*
    requirements {
        contains("teamcity.agent.name", "aaaaa")
    }
*/
})
