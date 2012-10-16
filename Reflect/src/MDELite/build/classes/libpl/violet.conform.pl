/* file violet.conform.pl */

/* here's the schema of violet
table(violetClass,[id,"name","fields","methods",superid,x,y]).
table(violetInterface,[id,"name","methods",x,y]).
table(violetAssociation,[cid1,"role1",arrow1,type1,cid2,"role2",arrow2,type2,lineStyle]).
*/

/* uniqueNames CONSTRAINT: Class, and Interfaces have unique names constraint */

findPair(I1,I2,N):-ename(I1,N),ename(I2,N),@I1 @< @I2.
uniqueNames:-forall(findPair(_,_,N),isError('unique names constraint violated: ',N)).


/* circular CONSTRAINT: Check circularity of class Hierarchy */

/*  utility predicates */
isClass(I,N):-violetClass(I,N,_,_,_,_,_).
isIntf(I,N):-violetInterface(I,N,_,_,_).
ename(I,N):-isIntf(I,N);isClass(I,N).

subClass(Sub,Sup):-violetAssociation(Sup,_,'TRIANGLE',_,Sub,_,_,_,_),isClass(Sup,_),isClass(Sub,_).
subClass(Sub,Sup):-violetAssociation(Sub,_,_,_,Sup,_,'TRIANGLE',_,_),isClass(Sup,_),isClass(Sub,_).
subOf(Sub,Sup):-subClass(Sub,Sup).
subOf(Sub,Sup):-subClass(Sub,X),subOf(X,Sup).

testCycle(I,N):-subOf(I,I),isError('circular class hierarchy: ',N).
testCycle(_,_).

circular:-forall(isClass(I,N),testCycle(I,N)).

/* icircular CONSTRAINT: Check circularity of inheritance Hierarchy  */

subIntf(Sub,Sup):-violetAssociation(Sup,_,'TRIANGLE',_,Sub,_,_,_,_),isIntf(Sup,_),isIntf(Sub,_).
subIntf(Sub,Sup):-violetAssociation(Sup,_,'TRIANGLE',_,Sub,_,_,_,_),isIntf(Sup,_),isIntf(Sub,_).
isubOf(Sub,Sup):-subIntf(Sub,Sup).
isubOf(Sub,Sup):-subIntf(Sub,X),isubOf(X,Sup).

testICycle(I,N):-isubOf(I,I),isError('circular interface hierarchy: ',N).
testICycle(_,_).

icircular:-forall(isIntf(I,N),testICycle(I,N)).

/* noDoubleEnds CONSTRAINT: Check that no edge has diamond, arrow, triangle, etc on both sides */

hasEndShape('BLACK_DIAMOND').
hasEndShape('TRIANGLE').
%hasEndShape('V').
hasEndShape('DIAMOND').

testHasDoubleEnds(I,N):- violetAssociation(I,_,Arrow1,_,ToNodeId,_,Arrow2,_,_),
                            hasEndShape(Arrow1), hasEndShape(Arrow2),
                                violetClass(ToNodeId,ToNodeName,_,_,_,_,_),
                                    isError('double ended edge: ',[N,ToNodeName]).
testHasDoubleEnds(_,_).

noDoubleEnds :- forall(isClass(I,N),testHasDoubleEnds(I,N)).
inoDoubleEnds :- forall(isIntf(I,N),testHasDoubleEnds(I,N)).

/* legalMultiplicity CONSTRAINT: Check the legality of the multiplicity */

noMult('').

isTriangle('TRIANGLE').

testMultiplicity(I,N):- violetAssociation(I,Mult1,Arrow1,_,_,Mult2,Arrow2,_,_),
                            (\+ noMult(Mult1);\+ noMult(Mult2)),
                                (isTriangle(Arrow1);isTriangle(Arrow2)), 
                                    isError('illegal multiplicity: ',N).
testMultiplicity(_,_).

legalMultiplicity :- forall(isClass(I,N),testMultiplicity(I,N)).
ilegalMultiplicity :- forall(isIntf(I,N),testMultiplicity(I,N)).

/* ALL CONSTRAINTS */

run:-uniqueNames,circular,icircular,
        noDoubleEnds,inoDoubleEnds,
            legalMultiplicity,ilegalMultiplicity.
