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

Add/append shell command:

    gem install brakeman --no-rdoc --no-ri &&
    brakeman -o brakeman-output.tabs

Enable Brakeman plugin.
