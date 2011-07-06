A Hudson/[Jenkins](http://jenkins-ci.org) plugin to run [Brakeman](https://github.com/presidentbeef/brakeman) against Ruby on Rails applications and track the results.

## Requirements

This plugin requires the [Static Analysis Utilities Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Static+Code+Analysis+Plug-ins) (which is also called "analysis-core") version >= 1.16. The plugin is available in Jenkins under `Manage Jenkins -> Manage Plugins -> Available -> Static Analysis Utilities`.

You will also need to have Ruby and RubyGems installed on the Jenkins/Hudson server.

## Installing the Plugin

Download `brakeman.hpi` from [here](https://github.com/downloads/presidentbeef/brakeman-jenkins-plugin/brakeman.hpi).

Inside Jenkins, go to `Manage Jenkins -> Manage Plugins -> Advanced -> Upload Plugin`.

Upload `brakeman.hpi`.

## Usage

Add/append a shell command to your job configuration like:

    gem install brakeman --no-rdoc --no-ri &&
    brakeman -o brakeman-output.tabs

Or, if [rvm](https://rvm.beginrescueend.com/) is available:

    bash -l -c 'rvm use 1.8.7 &&
    rvm gemset create brakeman &&
    rvm gemset use brakeman &&
    gem install brakeman --no-rdoc --no-ri &&
    brakeman -o brakeman-output.tabs'

There is also a smaller package called `brakeman-min` which installs a minimal version of Brakeman. If Haml or RailsXSS are used, then the `haml` or `erubis` gems (respectively) need to be installed separately.

Click 'Publish Brakeman warnings' to enable the Brakeman.

Some adjustment may need to be done regarding paths. Brakeman needs to be run at the root of the application or supplied with the path. The output file specified on the commandline needs to be synched with the output file specified as a plugin option.

## Testing without Existing Jenkins Server

Requires Maven2. You will likely need to modify your environment as documented [here](https://wiki.jenkins-ci.org/display/JENKINS/Plugin+tutorial#Plugintutorial-SettingUpEnvironment).

After cloning the source, run this in the main directory:

    mvn hpi:run

This starts up a copy of Jenkins with the Brakeman plugin installed. This is not necessary if you just want to install the plugin.

## Build

Requires Maven2. You will likely need to modify your environment as documented [here](https://wiki.jenkins-ci.org/display/JENKINS/Plugin+tutorial#Plugintutorial-SettingUpEnvironment).

After cloning the source, run this in the main directory:

    mvn install

This builds a copy of the plugin in `./target/brakeman.hpi`
