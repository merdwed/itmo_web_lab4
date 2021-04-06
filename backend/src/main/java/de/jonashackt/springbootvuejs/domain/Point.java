package de.jonashackt.springbootvuejs.domain;
import de.jonashackt.springbootvuejs.exception.*;
import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="t_point")
public class Point implements Serializable{
// PrimaryKey
protected Point(){
}
public Point( long userId, long coordX, double coordY){
     this.userId=userId;
     this.coordX=coordX;
     this.coordY=coordY;
}
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private long id;
private long userId;
private long coordX;
private double coordY;
@Override
public String toString(){
    return "id:" + id + ", userId:" + userId + ", coordX:" + coordX + ", coordY:" + coordY;
}
public void check(){
    if(coordY<-3 || coordY>5)throw new IllegalParamException("incorrect parameter X:" + coordY);
}
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
