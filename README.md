# jepsen.neo4j

Messing around with [Jepsen](https://jepsen.io/) tests for Neo4j.
Currently following the etcd [tutorial](https://github.com/jepsen-io/jepsen/tree/main/doc/tutorial).
Jepsen [docs](https://cljdoc.org/d/jepsen/jepsen/0.1.15/api/jepsen).

Currently grabs a neo4j community instance, creates 1 node and reads/writes a property on it.
Testing that the result is linearizable.

## Installation

Download from http://example.com/FIXME.

## Usage

1. Start VMs in Azure.
2. Load tarball on each VM.
3.

Run a test:

    $ lein run test --node <public_ip> --ssh-private-key <private_key> --username <user>

## Options

Supply a public IP and ssh creds.

## Examples

...

### Bugs

...

### Test Scaffolding

Assumes 3 VMs are running in Azure, each with a Neo4j tarball.
All necessary ports must be open. TODO: list them.

### Database Automation

1. Extract files from tarball.
2. Set the following config parameters in `conf/neo4j`:
```
dbms.default_listen_address=0.0.0.0
dbms.default_advertised_address=<public-ip>
dbms.mode=CORE
causal_clustering.initial_discovery_members=<public-ip-1>:5000,<public-ip-2>:5000,<public-ip-3>:5000
```
3. Start the DBMS.
4. Shutdown the DBMS.
5. Delete the files.

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
