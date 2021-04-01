package de.jonashackt.springbootvuejs.domain;

import javax.persistence.*;

@Entity
@Table(name="t_point")
public class Point {
// PrimaryKey
protected Point(){
}
public Point( long userId, long paramR, long coordX, double coordY){
     this.userId=userId;
     this.paramR=paramR;
     this.coordX=coordX;
     this.coordY=coordY;
}
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private long id;
private long userId;
private long paramR;
private long coordX;
private double coordY;
public void setId(long id){
    this.id=id;
}
public long getId(){
    return this.id;
}
public void setUserId(long userId){
    this.userId=userId;
}
public long getUserId(){
    return this.userId;
}
public void setParamR(long paramR){
    this.paramR=paramR;
}
public long getParamR(){
    return this.paramR;
}
public void setCoordX(long coordX){
    this.coordX=coordX;
}
public long getCoordX(){
    return this.coordX;
}
public void setCoordY(double coordY){
    this.coordY=coordY;
}
public double getCoordY(){
    return this.coordY;
}
}
