w4-wfsuite-tool-genactors
=========================

The goal of this tool is to import actors/roles/domains in W4 BPM Suite Engine (aka. W4 Business First)
from an Excel File. 



Download
--------

The package is available from the [release page](https://github.com/w4software/w4-wfsuite-tool-genactors/releases)



Requirements
------------

You will need a JRE (Java Runtime Environment) installed and available prior to running it.



Installation
------------

To install it, you only need to extract the binary package (zip or tar.gz).



Usage
-----

### Excel file

The source used to define which actors, roles and/or domains should be created are defined in an Excel file. This file should be saved with Excel 97-2003 Compatibility mode (.xls format).

Each line but the first should contain an actor, role or domain to import. The first line should be the name of the column which must either be

- an attribute name of either `TWFactor`, `TWFrole` or `TWFdomain`. 
- a column named `TYPE` that will contain the entry type for the line which can either be `ACTOR`, `ROLE` or `DOMAIN` (default to `ACTOR`)
- a column named `ROLE` that will contains the role that the actor must be assigned. The syntax of a role is either `ROLE_NAME` or `DOMAIN_NAME:ROLE_NAME`

Refer to examples below for more details. 

#### Simple structure (actors only)

| str | first name | last name    | password  | role         | role             | manager |
| --- | ---------- | ------------ | --------  | -----------  | ---------------- | ------- |
| ll  | lex        | luthor       | abc       | declared     | coordinator      |         |
| ak  | andrei     | kalashnikov  | def       | declared     | administrator    | ak      |


#### Complete structure

| TYPE   | STR            | UPPER DOMAIN   | FIRST NAME | LAST NAME | MANAGER | ROLE                     | ROLE                       |
| ----   | ----           | ----           | ----       | ----      | ----    | ----                     | ----                       | 
| DOMAIN | BUSINESS_UNITS | global         |            |           |         |                          |                            | 
| DOMAIN | BU_PROCESS     | BUSINESS_UNITS |            |           |         |                          |                            | 
| DOMAIN | BU_APPLICATION | BUSINESS_UNITS |            |           |         |                          |                            | 
| DOMAIN | BU_DATABASE    | BUSINESS_UNITS |            |           |         |                          |                            | 
| ROLE   | MANAGER        |                |            |           |         |                          |                            | 
| ROLE   | DEVELOPER      |                |            |           |         |                          |                            | 
| ROLE   | TESTER         |                |            |           |         |                          |                            | 
| ACTOR  | jls            |                | Jean       | Lasaret   |         | administrator            | coordinator                | 
| ACTOR  | ehe            |                | Etienne    | Heurice   | jls     | BU_PROCESS:MANAGER       | BU_PROCESS:coordinator     | 
| ACTOR  | ban            |                | Benoit     | Anhroussel| jls     | BU_APPLICATION:MANAGER   | BU_APPLICATION:coordinator | 
| ACTOR  | dmo            |                | David      | Moutier   | ban     | BU_APPLICATION:DEVELOPER |                            | 
| ACTOR  | njo            |                | Nolwenne   | Joella    | ehe     | BU_PROCESS:DEVELOPER     |                            | 
| ACTOR  | rco            |                | Regis      | Cochois   | jls     | BUSINESS_UNITS:TESTER    | BU_DATABASE:DEVELOPER      |
| ACTOR  | ilo            |                | Irène      | Longuet   | ehe     | BU_PROCESS:DEVELOPER     | BU_PROCESS:TESTER          |


### Command line

Generation of actors/roles and domains is done from shell command line (either cmd in Windows or *sh under *nix).

The following syntax has to be used under 

    genactors.<cmd/sh> -f <filename.xls>

The `-f` parameter should point to the Excel file which must be in the format explained above.

#### Connection options

Following command line options are available as connection parameters

- -s w4_engine_host (default to localhost)
- -i w4_engine_instance (default to w4adm)
- -l w4_login (default to w4adm)
- -w w4_password (default to w4adm)

#### Setting modes

Following command options are available to configure the generation mode:

- -a  mode_actors
- -r  mode_roles
- -d  mode_domains
- -aa mode_assignation

This options allow to define the expected creation/update mode for each type of entry.
Each mode is given with some letters having each its own meaning described below
(e.g.: `-a cup -r cu -aa au`).

When nothing should be done for a particular type of entry (i.e. ignore this type of 
entry),a minus sign (`-`) should be given as mode (e.g.: `-a - -r -`).

##### Actors mode (default: CU)

__C__: create inexistant actors

__U__: update attributes of existing actors

__P__: also update password (require U mode)


##### Roles mode (default: CU)

__C__: create inexisting roles

__U__: update existing roles

__I__: include in role list to create/update the roles which are defined as values of  
`ROLE` column(s)

##### Domains mode (default: CU)

__C__: create inexisting domains

__U__: update existing domains

__I__: include in domain list to create/update the domains which are defined as prefix of values 
of `ROLE` column(s) (with syntax `DOMAIN:ROLE`). Beware that domains created this way will all be
created as direct children of `global` (root) domain.

##### Assignation mode (default: A)

__A__: assign all roles mentionned in `ROLE` columns (in the mentionned domain if specified)

__U__: unassign all roles of actor before assigning new ones (no role other than the ones defined
in the excel file will be left).


### Examples

    genactors.cmd -f Actors.xls

    genactors.cmd -f Actors.xls -a - -r cui -d cu -aa au



Building from source
--------------------

### Requirements
Sun/Oracle Java JDK 1.5+ and [Apache Ant](http://ant.apache.org/) are required to build the sources.


### Retrieving public dependencies

Run the following command to retrieve public dependencies.

    ant ivy

As it require a connection to Internet, you may have to add additionnal parameters to setup a proxy if you have no direct access from the compiling host.

_As an alternative, you can check required dependencies in `ivy.xml` and manually download them to `lib/default` subfolder._


### Retrieving W4 depencencies

Run the following command to retrieve W4 API artifacts.

    ant wfjlib -DW4_HOME=c:/W4Engine

where `W4_HOME` should point to the installation directory of W4 Engine.

_As an alternative you can copy wfjlib directly to lib/w4 subfolder._


### Building

Build process will produce a zip package (for Windows), a gziped tarball (for *nix) and the raw jar file which can be used on any OS directly with `java`.

    ant delivery


License
-------

Copyright (c) 2008-2015, W4 S.A. 

This project is licensed under the terms of the MIT License (see LICENSE file)

Ce projet est licencié sous les termes de la licence MIT (voir le fichier LICENSE)