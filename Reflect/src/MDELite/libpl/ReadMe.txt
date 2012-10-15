This is the directory of all prolog rule files.
The following Sections describe basic file types in 
this directory

-------------.schema.pl files--------------------------------
Files that have the name <ObjectType>.schema.pl  define
the prolog tables of the <ObjectType> database (e.g., sdb, yuml, etc.)
Let's look at yuml.schema.pl as an example, with comments unindented

    :-dynamic yumlClass/4, yumlInterface/3, yumlAssociation/6.

The above line declares that yuml.schema has 3 tables;
one is yumlClass with 4 attributes, yumlInterface with 3
attributes and yumlAssociation with 6 attributes.
The dynamic declaration is needed to tell Prolog that there are such
tables, even in the case when they are empty.  Without this
declaration, Prolog will issue an error if it tries to access an empty table.

    /* these tables define a Yuml database */

    schema(yumlClass,[id,"name","fields","methods"]).
    schema(yumlInterface,[id,"name","methods"]).
    schema(yumlAssociation,["name1","role1","end1","name2","role2","end2"]).

The above is a standard form for table declarations.  Attributes whose
names are "quoted" will have single-quoted values 'like this', and will
have '' for null values.  Attributes whose names are not quoted (e.g., id)
will be unquoted prolog atoms.  Note double-quoted strings are never used
in table tuples.

    tuple(yumlClass,L):-yumlClass(I,N,F,M),L=[I,N,F,M].
    tuple(yumlInterface,L):-yumlInterface(I,N,M),L=[I,N,M].
    tuple(yumlAssociation,L):-yumlAssociation(N1,R1,E1,N2,R2,E2),L=[N1,R1,E1,N2,R2,E2].

The above 3 rules is a standard form for printing tuples of such tables.
I believe they can be "generated" from a schema tuple, but right now, they
have to be hand-written.

-------------.conform.pl files--------------------------------
Every database has a set of constraints that must be satisfied to make it "legal".
(In MDE terms, these are the constraints that models must satisfy to conform to
their metamodels).

So yuml.conform.pl are the constraints, written in prolog, that must be satisfied
for a yuml database to conform to its schema.  All of the conform.pl files that
are present rather similar, so look at any of them to get an idea of how constraints
are written.

------------------------run.pl files---------------------------
Files with name <ObjectType>.run.pl define the prolog rules for executing
prolog.  Normally, a single rule is defined, called "run;-" which generally
prints the tables of a database that is synthesized.  Let's look at
yuml.run.pl as an example; comments are unindented.

    /* output the tables of a Yuml database */

    run:-printTables([yumlClass,yumlAssociation,yumlInterface]).

This says print all tables of a yuml prolog database.  Note that each
table has to be listed explicitly, just in case.  The print.pl prolog
file will print all tables, but may end up printing tables that you don't
need.

-------------------------A2B.pl files-------------------------

Files of the form <ObjectTypeA>2<ObjectTypeB> define the rules for translating
an database of type ObjectTypeA  to a database of ObjectTypeB.

------------------------print.pl------------------------------
This is key file for printing tables.  Generally, you use the rule:

    printTable([<table list>]

to print the tables of a database.  Now the way printTable works is
that it searches for each schema() declaration, which is a table
declaration, and for each such schema-fact, there is a tuple-fact
that converts a tuple into a printable format.  

To project (remove) a table from a database, don't list it in 
the <table list> above.

Occasionally, some transformation is made to table X that produces
another instance of table X.  Well, in Prolog, you can't do this
directly. What you can do is produce table X1 as a translation of
table X.  Now, to print X1 as an X table, define this rule
for each such table pair:

    tableAlias(X1,X).

This tells the print rules to print "X" instead of "X1" when 
the tuples of X1 are being printed.