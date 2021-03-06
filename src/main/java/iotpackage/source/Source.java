package iotpackage.source;


import iotpackage.IPInfo;
import iotpackage.destination.Destination;

public class Source  extends IPInfo {


    public Source(String id, String ip) {
        super(id, ip);
    }

    public Source(){
        super();
    }

    public  Destination changeToDestination(){
        return new Destination(getId(),getIp());
    }

    public Source(IPInfo ipInfo){
        super(ipInfo.getId(),ipInfo.getIp());
    }

    public void printSource(){
        System.out.println(">--------------");
        System.out.println("the Source:");
        System.out.println(">>Id:"+getId());
        System.out.println(">>IP:"+getIp());
        System.out.println(">--------------");
    }

}

