package com.itdom.prototype;

public class PrototypeModelTest {
    public static void main(String[] args) throws CloneNotSupportedException {
        ThreeCircumference threeCircumference = new ThreeCircumference();
        threeCircumference.setBust(40);
        threeCircumference.setHipline(40);
        threeCircumference.setWaist(30);
        InflatableDoll inflatableDoll = new InflatableDoll();
        inflatableDoll.setName("mimi");
        inflatableDoll.setNo("123435434");
        inflatableDoll.setThreeCircumference(threeCircumference);
        System.out.println(inflatableDoll.hashCode()+"|"+inflatableDoll.toString());
        InflatableDoll clone = (InflatableDoll)inflatableDoll.clone();
        System.out.println(clone.hashCode()+"|"+clone.toString());
        inflatableDoll.getThreeCircumference().setWaist(50);
        inflatableDoll.getThreeCircumference().setHipline(50);
        inflatableDoll.getThreeCircumference().setBust(50);
        System.out.println(inflatableDoll.hashCode()+"|"+inflatableDoll.toString());
        System.out.println(clone.hashCode()+"|"+clone.toString());
    }
}
class InflatableDoll implements Cloneable{
    private String name;
    private String no;
    private ThreeCircumference threeCircumference;

    public InflatableDoll() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public ThreeCircumference getThreeCircumference() {
        return threeCircumference;
    }

    public void setThreeCircumference(ThreeCircumference threeCircumference) {
        this.threeCircumference = threeCircumference;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        InflatableDoll clone = (InflatableDoll)super.clone();
        clone.setThreeCircumference(clone.getThreeCircumference().clone());
        return clone;
    }

    @Override
    public String toString() {
        return "InflatableDoll{" +
                "name='" + name + '\'' +
                ", no='" + no + '\'' +
                ", threeCircumference=" + threeCircumference +
                '}';
    }
}
class ThreeCircumference implements Cloneable{
    private double bust;

    private double waist;

    private double hipline;

    public ThreeCircumference() {
    }

    public double getBust() {
        return bust;
    }

    public void setBust(double bust) {
        this.bust = bust;
    }

    public double getWaist() {
        return waist;
    }

    public void setWaist(double waist) {
        this.waist = waist;
    }

    public double getHipline() {
        return hipline;
    }

    public void setHipline(double hipline) {
        this.hipline = hipline;
    }

    @Override
    protected ThreeCircumference clone() throws CloneNotSupportedException {
        return (ThreeCircumference)super.clone();
    }

    @Override
    public String toString() {
        return "ThreeCircumference{" +
                "bust=" + bust +
                ", waist=" + waist +
                ", hipline=" + hipline +
                '}';
    }
}