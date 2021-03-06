package iotpackage.data.ciphertext;

/**
 * @author hi
 * */
public class Lifetime {
    /**
     *
     * */
    String id;

    /**
     * 时间戳
     * **/
    String context;

    public void printfLifetime(){
        System.out.println(">--------------");
        System.out.println("the lifetime:");
        System.out.println(">>id:"+getId());
        System.out.println(">>context:"+getContext());
        System.out.println(">--------------");
    }

    public Lifetime(String id, String context) {
        this.id = id;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
