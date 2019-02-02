# jsongen

Jsongen is a quick and dirty JSON data generator for benchmarking 
purposes.

## Getting started

Jsongen constructs tree types of data set types: *structured*, 
*unstructured*, and *document*, and let the caller specify 
particular variables such as the number of attributes being 
generated. 

To get Jsongen run the following in your terminal:
```
$ wget https://github.com/protolabs/jsongen/archive/master.zip
$ unzip master.zip
$ cd jsongen-master/
```

The general usage is

```
$ java -jar bin/jsongen.jar $type $format $n $m $params
```

The parameter `$type` controlls which dataset type should be 
generated. Valid values are `structured`, `bound-structured`, 
`unstructured` and `document`. 
The parameter `$format` specifies whether a (canonical) key-value 
pair serialization should be used, or whether values to keys should 
be stored as an array. To use a key-value-pair serialization, set 
`$format` to `coc` (canonical object collection), or `kvc` 
(key-centric value collection). The parameters `$n`, `$m`, and 
`$params` depend on `$type` and are described below.

### Formats

For ease of use, consider two objects where the first has two 
properties `a` and `b` 
while the second shares the property `b` with the first object, 
misses `a`, and has 
an additional property `c`. 

The format controls how data is represented and formatted. 

#### Canonical Key-Value Pair (COC)

A COC-formatted dataset is a plain-text file where each line is a valid JSON
object. This format is the typical form when using JSON files or
similar formats.

The example dataset from above looks like the following when using
COC:

```
{"a": 1, "b": 2}
{"b": 3, "c": 4} 
```

#### Key-Centric Value Collection (KVC)

In contrast to COC, a KVS formatted dataset is a single valid JSON
object that stores all values to a particular key across all objects
in a dataset. Especially for "tabular" data (as it the case for
CSV data, SQL table dumps, sensor data), KVC is a reasonable format.
For semi-structured/unstructured data, this format fills missing
values with `null` which lead to a notable waste of storage the
more unstructured a dataset become.

The example dataset from above looks like the following when using
KVC:

```
{
   "a": [1, null],
   "b": [2, 3],
   "c": [null, 4] 
}
```

### Structured Datasets

**Definition**: Given positive integers `n` and `m`, a structured dataset
is a collection of `n` objects each with one property `(x, vi)` 
where `vi` for a key `x` (in `i=1..n`) is an 
(uniform randomly chosen) integer value. Each object has further
`m` padding properties `(ai, X)` (for `i=1..m`) where the value `X` has some unspecified integer 
value. 

Using a JSON format, a structured dataset looks like the following example:

`[ {"x": 145451212, "a1": -45454512}, {"x": 3455, "a1": 464565145} ]`

#### Example (COC)
To generate a COC-formatted structured datasets with `n=3` objects and `m=4` padding 
attributes, run the following in your terminal:
```
$ java -jar bin/jsongen.jar structured coc 3 4 
```
The following output will printed to `stdout`:
```
{"x": -1855663928, "a0": -1227925714, "a1": 925399010, "a2": -1731108131, "a3": 1589968364}
{"x": -1437458954, "a0": -307694383, "a1": -1420140828, "a2": -1496942182, "a3": 665380447}
{"x": -1262073224, "a0": -1362227056, "a1": 1210455822, "a2": 214672973, "a3": -978523739}
```
#### Example (KVC)
To generate a KVC-formatted structured datasets with `n=3` objects and `m=4` padding 
attributes, run the following in your terminal:
```
$ java -jar bin/jsongen.jar structured kvc 3 4 
```
The following output will printed to `stdout`:
```
{
    "x": [-964403861, 979632107, 1729993179], 
    "a0": [855412270, -1006063988, 772233486], 
    "a1": [-778489023, 1324423887, 2121931000], 
    "a2": [-1670005764, -1852942822, 128131099], 
    "a3": [1012130970, 1851118563, 2013534247]
}
```

#### Example (bound)
For the purpose of micro-benchmarking value sets, the generator accepts
an additional value for `$type`, `bound-structured`. With
`bound-structured` being used, `$params` is `$lower` and `$upper`.
Both parameters define an closed interval `[lower, upper]` from which
values for properties in the objects are drawn.

To generate a COC-formatted bound-structured dataset with 
`n=3` objects, `m=4` padding attributes, and values within
 the range `-127` to `128`, run the following in your terminal:
```
$ java -jar bin/jsongen.jar bound-structured coc 3 4 -127 128
```
The following output will printed to `stdout`:
```
{"x": 63, "a0": -70, "a1": -106, "a2": 7, "a3": 41}
{"x": -3, "a0": 38, "a1": -59, "a2": 43, "a3": -8}
{"x": 30, "a0": 108, "a1": -48, "a2": -112, "a3": 64}
```
Alternatively, set `$format` to `kvc` to generate a KVC-formatted 
dataset.

### Unstructured Datasets

**Definition**: Given positive integers `n`, `m`, and `k`, an unstructured dataset is a 
collection of `n` objects each with `M < m` properties `(xi, v)` 
where `xi` is a key, and `v` is some unspecified integer value (for `i=1..M`). 
Any key is a random string out of a set of `k` random strings, and
`M` is uniform randomly drawn from `1` to `m`.

Using a JSON format, an untructured dataset looks like the following example:

`[ {"a": 1}, {"b": 2}, {"c": 45, "a": -221} ]`

#### Example (COC)
To generate a COC-formatted unstructured datasets with `n=3` objects and a per-object maximum of `m=4` padding 
attributes, and `k` random strings, run the following in your terminal:
```
$ java -jar bin/jsongen.jar unstructured coc 3 4 5 
```
The following output will printed to `stdout`:
```
{ "ugPPmizMgV": -244113780}
{ "ugPPmizMgV": -1985625522, "mqDEZPcF": 1058813889}
{ "qqR9I": -1515295321}
```
#### Example (KVC)
To generate a KVC-formatted structured datasets with `n=3` objects and a per-object maximum of `m=4` padding 
attributesand `k` random strings,  run the following in your terminal:
```
$ java -jar bin/jsongen.jar unstructured kvc 3 4 5 
```
The following output will printed to `stdout`:
```
{
   "ZPEvF0FkWAFQC": [null, -1856433223, 1011893521], 
   "CXcK0gNZ": [-853788014, -1072592816, -131780355], 
   "nXtxUuuW": [null, -523969042, null], 
   "eVbOQplo": [285049263, null, 915135070], 
   "eqbmInMlPOMj7": [-384336245, 1236182650, null]
}
```

### Document Datasets

**Definition**: Given positive integers `n`, `m`, and `k`, a 
document dataset is a collection of `n` objects each with one 
property `(x, v)`, `K < k` properties `(a, b)i` and an 
additional property `(y, v’)` where `x`, `y`, `a` are keys, 
`v`, `v’` are strings, and `b` is some integer value (for `i=1..K`).
 `K` is uniform randomly drawn from `1` to `k`. A value `v` is 
 constructed by concatenating `m` uniform randomly chosen strings 
 (whitespace separated).

Using a JSON format, an untructured dataset looks like the following example:

```
[ {"x": "Hello World", "a1": 2, "y": "Alice"}, 
  {"x": "Beautiful", "a1": -232, "a2": 442, "y": "Bob"} ]
```

#### Example (COC)
To generate a COC-formatted unstructured datasets with `n=3` objects and a per-object maximum of `k=5` padding 
attributes, and strings consisting of `m=4` random terms, run the following in your terminal:
```
$ java -jar bin/jsongen.jar document coc 3 4 5 
```
The following output will printed to `stdout`:
```
{ "x": "4yAXnha8TF 4yAXnha8TF SQNuRust1t pfoEbp7DGDG", 
  "y": "RkCXU" }
{ "x": "SQNuRust1t SQNuRust1t SQNuRust1t SQNuRust1t", 
  "a0": "SQNuRust1t pfoEbp7DGDG SQNuRust1t SQNuRust1t", 
  "a1": "4yAXnha8TF 4yAXnha8TF 4yAXnha8TF SQNuRust1t", 
  "y": "RkCXU" }
{ "x": "RkCXU 4yAXnha8TF pfoEbp7DGDG pfoEbp7DGDG", 
  "a0": "4yAXnha8TF 4yAXnha8TF RkCXU RkCXU", 
  "y": "SQNuRust1t"
}
```
#### Example (KVC)
To generate a KVC-formatted unstructured datasets with `n=3` objects and a per-object maximum of `k=5` padding 
attributes, and strings consisting of `m=4` random terms, run the following in your terminal:
```
$ java -jar bin/jsongen.jar document kvc 3 4 5 
```
The following output will printed to `stdout`:
```
{
   "x": ["g2XSO g2XSO ut1Dhn5 0rsO4eeMpRqp ut1Dhn5", 
         "g2XSO ut1Dhn5 g2XSO 0rsO4eeMpRqp 0rsO4eeMpRqp", 
         "0rsO4eeMpRqp g2XSO 0rsO4eeMpRqp ut1Dhn5 ut1Dhn5"], 
   "a0": ["0rsO4eeMpRqp aqdPjj6xR aqdPjj6xR g2XSO 0rsO4eeMpRqp", 
          null, 
          "g2XSO 0rsO4eeMpRqp 0rsO4eeMpRqp ut1Dhn5 0rsO4eeMpRqp"], 
   "a1": ["aqdPjj6xR g2XSO aqdPjj6xR aqdPjj6xR g2XSO", 
          null, 
          "0rsO4eeMpRqp ut1Dhn5 ut1Dhn5 aqdPjj6xR aqdPjj6xR"], 
   "a2": ["0rsO4eeMpRqp ut1Dhn5 g2XSO ut1Dhn5 g2XSO", 
          null, 
          "0rsO4eeMpRqp 0rsO4eeMpRqp aqdPjj6xR aqdPjj6xR 0rsO4eeMpRqp"], 
   "a3": ["aqdPjj6xR 0rsO4eeMpRqp ut1Dhn5 aqdPjj6xR g2XSO", 
          null, 
          "aqdPjj6xR 0rsO4eeMpRqp aqdPjj6xR aqdPjj6xR aqdPjj6xR"], 
   "y": ["aqdPjj6xR", "ut1Dhn5", "0rsO4eeMpRqp"]
}
```

## License

This project is licensed under the terms of the MIT LICENSE. See the LICENSE file.

## Donate

If you find tools in this repository helpful, and want to support the 
author with a donation: give a donation to [patreon.com/pinnecke](https://patreon.com/pinnecke)  ;)



## Contributing

- Discuss about your idea in a new issue
- Fork Jsongen (https://github.com/protolabs/jsongen)
- Create your one new feature branch (git checkout -b my-feature)
- Commit your changes (git commit -am 'Add some feature')
- Push to the branch (git push origin my-feature)
- Create a new Pull Request