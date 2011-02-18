[Hudson](http://hudson-ci.org) plugin to run [Brakeman](https://github.com/presidentbeef/brakeman) against Ruby on Rails applications and track the results.

## Test

Requires Maven2.

    mvn hpi:run

This starts up a copy of Hudson with the Brakeman plugin installed.

## Build

    mvn install

Builds a copy of the plugin in `./target/brakeman.hpi`

## Install

Inside Hudson, `Manage Hudson -> Manage Plugins -> Advanced -> Upload Plugin`

## Usage

Add/append a shell command like:

    gem install brakeman --no-rdoc --no-ri
    brakeman -o brakeman-output.tabs

Alternatively, there is a smaller package called `brakeman-min` which installs a minimal version of Brakeman. If Haml or RailsXSS are used, then the `haml` or `erubis` gems (respectively) need to be installed separately.

Enable Brakeman plugin.

Some adjustment may need to be done regarding paths. Brakeman needs to be run at the root of the application or supplied with the path. The output file specified on the commandline needs to be synched with the output file specified as a plugin option.

## Compatibility

Brakeman only supports Rails 2.x at the moment. Rails 3 support is currently underway.
