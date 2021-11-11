# jepsen.neo4j

Messing around with [Jepsen](https://jepsen.io/) tests for Neo4j. 
Currently following the etcd [tutorial](https://github.com/jepsen-io/jepsen/tree/main/doc/tutorial).
Jepsen [docs](https://cljdoc.org/d/jepsen/jepsen/0.1.15/api/jepsen).

Currently grabs a neo4j community instance, creates 1 node and reads/writes a property on it.
Testing that the result is linearizable.

## Installation

Download from http://example.com/FIXME.

## Usage

Run a test:

    $ lein run test --node <public_ip> --ssh-private-key <private_key> --username <user>

## Options

Supply a public IP and ssh creds.

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2021 Jack Waudby

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
