/* file: umlf2sdb.pl */

:-style_check(-discontiguous).

%table(violetClass,[id,"name","fields","methods",superid,x,y]).
%table(violetInterface,[id,"name","methods",x,y]).
%table(violetAssociation,[cid1,"role1",arrow1,type1,cid2,"role2",arrow2,type2,lineStyle]).

/*********** translation of violetClass to class tuples *************/
class(IName,F,N,M,Superid):-violetClass(I,F,N,M,S,_,_), S \== '',concat_atom(['\'',S,'\''],Superid),concat_atom(['\'',I,'\''],IName).
class(IName,F,N,M,null):-violetClass(I,F,N,M,S,_,_), S == '',concat_atom(['\'',I,'\''],IName).
position(IName,X,Y):-violetClass(I,_,_,_,_,X,Y),concat_atom(['\'',I,'\''],IName).



/*********** translation of violetInterface to interface tuples *************/
interface(IName,N,M):-violetInterface(I,N,M,_,_),concat_atom(['\'',I,'\''],IName).
position(IName,X,Y):-violetInterface(I,_,_,X,Y),concat_atom(['\'',I,'\''],IName).

/*********** translation to violetAssociation to violetAssociation, classImplements, and interfaceExtends tuples *************/
xlate('V','\'arrow\'',_,_).
xlate('TRIANGLE','\'implem\'','classnode','interfacenode').
xlate('TRIANGLE','\'inherit\'','interfacenode',_).
xlate('TRIANGLE','\'inherit\'','classnode','classnode').
xlate('DIAMOND','\'agg\'',_,_).
xlate('BLACK_DIAMOND','\'comp\'',_,_).
xlate('','\'\'',_,_).
association(II1,R1,A1,II2,R2,A2):-violetAssociation(I1,R1,Arrow1,Type1,I2,R2,Arrow2,Type2,_),
                    xlate(Arrow1,A1,Type1,Type2),xlate(Arrow2,A2,Type1,Type2),
                        concat_atom(['\'',I1,'\''],II1),concat_atom(['\'',I2,'\''],II2).
classImplements(C,I):-violetClassImplements(C,I).
interfaceExtends(C,P):-violetInterfaceExtends(C,P).
