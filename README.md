# nsp4j-api

## Network and Service management Planning framework for Java - API


### Configuration

Example configuration files: 

- `example.dgs`: topology description file
- `example.txt`: paths file
- `example.yml`: parameters file


### Topology file

The network topology is specified using GraphStream guidelines, for example:

```
DGS004
test 0 0

an n1 x:100 y:150 num_servers:1 server_capacity:1000 
an n2 x:150 y:100
an n3 x:200 y:100

ae n1n2 n1 > n2 capacity:1000
ae n2n3 n2 > n3

```

- `an` adds a node. The command is followed by a unique node identifier, that can be a single word or a string delimited by the double quote character. Values x and y on the server represent the coordinates of the nodes. For each node other parameters can be specified, for instance, the number of servers or the server capacity.

- `ae` adds an link. This command must be followed by a unique identifier of the link, following with the identifiers of two connecting nodes. For each link, other parameters can be specified, for instance, the link capacity 

For further information, see [Graphstream](http://graphstream-project.org/doc/Advanced-Concepts/The-DGS-File-Format/) documentation.


### Paths file

This file contains all admissible paths for the topology, for example:

```
[n1, n2, n3, n7, n9]
[n1, n4, n5, n3, n7, n9]
[n1, n4, n5, n6, n7, n9]
```


### Parameters file

This file describes the parameters for the optimization model, for example:

```
# optimization parameters
gap: 0
weights: [0, 1.0, 0]
# auxiliary parameters
aux: {
  "iterations": 1000,
  "offset_results": 0,
  "scaling_x": 1.0,
  "scaling_y": 1.0
}
# service definitions
serviceChains:
- id: 0
  chain: [0, 1, 2]
  attributes: {
    "minPaths": 1,
    "maxPaths": 2
  }
# function definitions
functionTypes:
- type: 0
  attributes: {
    "replicable": false,
    "load": 1.0,
    "overhead": 10,
    "sync_load": 0.1,
    "delay": 10
  }
- type: 1
  attributes: {
    "replicable": true,
    "load": 1.0,
    "overhead": 10,
    "sync_load": 0.1,
    "delay": 10
  }
# traffic flow definitions
traffic_flows:
  - min_dem: 1
    max_dem: 3
    min_bw: 1
    max_bw: 20 # change "max_cap_server" accordingly
    services: [1]
    service_length: [7]

```