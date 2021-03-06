package iotpackage.constructor;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import iotpackage.IoTKey;
import iotpackage.data.TS;
import iotpackage.data.autheticator.Authenticator;
import iotpackage.data.ciphertext.Lifetime;
import iotpackage.data.fuction.Email;
import iotpackage.data.fuction.User.User;
import iotpackage.data.fuction.emailList.EmailList;
import iotpackage.data.ticket.Ticket;
import iotpackage.destination.Destination;
import iotpackage.source.Source;
import securityalgorithm.DESUtil;

public class CipherConstructor {
    /* ObjectNode CipherNode ;
     String context;
     String id;*/
    //对包加密的key
    String cipherKey;

    //JsonNodeFactory 实例，可全局共享
    private JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
    //JsonFactory 实例，线程安全
   // private JsonFactory jsonFactory = new JsonFactory();

    /**工具函数*/
    //source节点添加
    void setSourceNode(ObjectNode parentNode, Source source){

        ObjectNode sourceNode = jsonNodeFactory.objectNode();
        sourceNode.put("Id",source.getId());
        sourceNode.put("IP",source.getIp());
        parentNode.set("Source",sourceNode);
    }

    //destination节点添加
    void setDestionationNode(ObjectNode parentNode, Destination destination){
        ObjectNode destinationNode=jsonNodeFactory.objectNode();
        destinationNode.put("Id",destination.getId());
        destinationNode.put("IP",destination.getIp());
        parentNode.set("Destination",destinationNode);
    }

    //Key节点添加
    void setKeyNode(ObjectNode parentNode,IoTKey key){
        ObjectNode iotKeyNode=jsonNodeFactory.objectNode();
        iotKeyNode.put("Id",key.getId());
        iotKeyNode.put("Context",key.getContext());
        parentNode.set("Key",iotKeyNode);
    }

    //时间戳节点添加
    void setTSNode(ObjectNode parentNode,TS ts){
        ObjectNode TSNode=jsonNodeFactory.objectNode();
        TSNode.put("Id",ts.getId());
        TSNode.put("Context",ts.getContext());
        parentNode.set("TS",TSNode);

    }


    //Lifetime节点添加
    void setLifetimeNode(ObjectNode parentNode, Lifetime lifetime){
        ObjectNode TSNode=jsonNodeFactory.objectNode();
        TSNode.put("Id",lifetime.getId());
        TSNode.put("Context",lifetime.getContext());
        parentNode.set("Lifetime",TSNode);

    }
    //Ticket节点添加
    void setTicketNode(ObjectNode parentNode, Ticket tgs,String ticketID,String ticketKey) throws JsonProcessingException {
        ObjectNode TSNode=jsonNodeFactory.objectNode();
        TSNode.put("Id",ticketID);
        TSNode.put("Context",getCipherOfTicket(tgs,ticketKey));
        parentNode.set("Ticket",TSNode);

    }

    /***
     * Email 节点生成强加密
     * @param parentNode
     * @param email
     * @param emailID
     * @param emailKey
     * @throws JsonProcessingException
     */
    void setEmailNode(ObjectNode parentNode, Email email,String emailID,String emailKey) throws JsonProcessingException {
        ObjectNode TSNode=jsonNodeFactory.objectNode();
        TSNode.put("Id",emailID);
        TSNode.put("Context",getCipherOfEmail(email,emailKey));
        parentNode.set(email.getClass().getSimpleName(),TSNode);

    }

    /***
     * Email 节点生成
     * @param parentNode
     * @param email
     * @throws JsonProcessingException
     */
    void setEmailNode(ObjectNode parentNode, Email email) throws JsonProcessingException {
        ObjectNode EmailNode=jsonNodeFactory.objectNode();
        EmailNode.put("Email",getPackageEmailToGson(email));
        parentNode.set(email.getClass().getSimpleName(),EmailNode);

    }

    ObjectNode getEmailNode(ObjectNode parentNode,Email email) throws JsonProcessingException {
        ObjectNode TSNode=jsonNodeFactory.objectNode();
        // ObjectNode rootNode = jsonNodeFactory.objectNode();
        parentNode.put("Id",email.getId());
        setUser(parentNode,email.getSender());
        setUser(parentNode,email.getReceiver());
        parentNode.put("Title",email.getTitle());
        parentNode.put("Time",email.getTime());
        parentNode.put("Type",email.getType());
        parentNode.put("Context",email.getContext());
        return parentNode;
    }

    ObjectNode getEmailNode(Email email) throws JsonProcessingException {
        //ObjectNode TSNode=jsonNodeFactory.objectNode();
        ObjectNode parentNode = jsonNodeFactory.objectNode();
        parentNode.put("Id",email.getId());
        setUser(parentNode,email.getSender());
        setUser(parentNode,email.getReceiver());
        parentNode.put("Title",email.getTitle());
        parentNode.put("Time",email.getTime());
        parentNode.put("Type",email.getType());
        parentNode.put("Context",email.getContext());
        return parentNode;
    }

    public ArrayNode getEmailListNode(EmailList emailList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        //ObjectNode rootNode = jsonNodeFactory.objectNode();
        for(int i=0;i<emailList.getListNumber();i++){
            array.add(getEmailNode(emailList.getEmailAtIndex(i)));
            //array.add((emailList.getEmailAtIndex(i)) );
        }

        return array;
    }

    public String getPackageTikectToGson(Ticket ticket) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        //rootNode.put("Id",tgs.getId())
        setKeyNode(rootNode,ticket.getKey());
        setSourceNode(rootNode,ticket.getId());
        setDestionationNode(rootNode,ticket.getAd());
        setTSNode(rootNode,ticket.getTs());
        setLifetimeNode(rootNode,ticket.getLifetime());
        return new ObjectMapper().writeValueAsString(rootNode);
    };

    public String getPackageAuthenticatorToGson(Authenticator authenticator) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        //rootNode.put("Id",tgs.getId())
        setDestionationNode(rootNode,authenticator.getId());
        setSourceNode(rootNode,authenticator.getAd());

        setTSNode(rootNode,authenticator.getTs());
        return new ObjectMapper().writeValueAsString(rootNode);
    };

    //邮件
    public void setUser(ObjectNode parentNode, User user){
        ObjectNode userNode=jsonNodeFactory.objectNode();
        userNode.put("Account",user.getAccount());
        userNode.put("Nickname",user.getNickname());
        parentNode.set(user.getClass().getSimpleName(),userNode);
    };
    public String getPackageEmailToGson(Email email) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        rootNode.put("Id",email.getId());
        setUser(rootNode,email.getSender());
        setUser(rootNode,email.getReceiver());
        rootNode.put("Title",email.getTitle());
        rootNode.put("Time",email.getTime());
        rootNode.put("Type",email.getType());
        rootNode.put("Context",email.getContext());

        //setTSNode(rootNode,authenticator.getTs());*/
        return new ObjectMapper().writeValueAsString(rootNode);
    };

    public String getPackageEmailListToGson(EmailList emailList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        for(int i=0;i<emailList.getListNumber();i++){
            array.add(getPackageEmailToGson(emailList.getEmailAtIndex(i)) );
        }
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
       // return new ObjectMapper().writeValueAsString(array);
        return objectMapper.writeValueAsString(array);
    }

    public String getCipherOfTicket(Ticket ticket,String ticketKey) throws JsonProcessingException {
        return DESUtil.getEncryptString(getPackageTikectToGson(ticket),ticketKey) ;
    }

    public String getCipherOfAuthenticator(Authenticator authenticator,String authenticatorKey) throws JsonProcessingException {
        return DESUtil.getEncryptString(getPackageAuthenticatorToGson(authenticator),authenticatorKey) ;
    }




    /***email 加密
     * @param email Email文件
     * @param emailKey 加密emailKey,为C和TGS共用的密钥
     * */
    public String getCipherOfEmail(Email email,String emailKey) throws JsonProcessingException {
        return DESUtil.getEncryptString(getPackageEmailToGson(email),emailKey) ;
    }

    /****AS->C*****/
    @Deprecated
    public String constructCipherOfAStoC(IoTKey ioTKey, String idTGS, TS ts,
                                         Lifetime lifetime, Ticket tgs,
                                         String ticketKey) throws JsonProcessingException {

        ObjectNode rootNode = jsonNodeFactory.objectNode();
        setKeyNode(rootNode,ioTKey);
        rootNode.put("IdTGS",idTGS);
        setTSNode(rootNode,ts);
        setLifetimeNode(rootNode,lifetime);
        // rootNode.put("Ticket",getCipherOfTicket(tgs,ticketKey));
        rootNode.put("Ticket",getCipherOfTicket(tgs,ticketKey));
        // return new ObjectMapper().writeValueAsString(rootNode);
        return DESUtil.getEncryptString(new ObjectMapper().writeValueAsString(rootNode),cipherKey);
    }

    public String constructCipherOfAStoC(IoTKey ioTKey, String idTGS, TS ts,
                                         Lifetime lifetime,
                                         Ticket tgs,String ticketID, String ticketKey) throws JsonProcessingException {

        ObjectNode rootNode = jsonNodeFactory.objectNode();
        setKeyNode(rootNode,ioTKey);
        rootNode.put("IdTGS",idTGS);
        setTSNode(rootNode,ts);
        setLifetimeNode(rootNode,lifetime);
        // rootNode.put("Ticket",getCipherOfTicket(tgs,ticketKey));
        setTicketNode(rootNode,tgs,ticketID,ticketKey);
        // return new ObjectMapper().writeValueAsString(rootNode);
        return DESUtil.getEncryptString(new ObjectMapper().writeValueAsString(rootNode),cipherKey);
    }


    /** TGS to C
     *
     * @param ioTKey 由TGS生成，供client和server之间信息的安全交换
     * @param IdV 确认该ticket是为server V签发的
     * @param ts 该值为4
     * @param ticketV V的ticket
     * @param ticketID V的ticket的编号
     * @param ticketKey 由TGS和V事先约定
     * @return
     * @throws JsonProcessingException
     */
    public String constructCipherOfTGStoC(IoTKey ioTKey, String IdV, TS ts,
                                          Ticket ticketV,String ticketID, String ticketKey) throws JsonProcessingException {

        ObjectNode rootNode = jsonNodeFactory.objectNode();
        setKeyNode(rootNode,ioTKey);
        rootNode.put("IdV",IdV);
        setTSNode(rootNode,ts);
        //setLifetimeNode(rootNode,lifetime);
        // rootNode.put("Ticket",getCipherOfTicket(tgs,ticketKey));
        setTicketNode(rootNode,ticketV,ticketID,ticketKey);
        // return new ObjectMapper().writeValueAsString(rootNode);
        return DESUtil.getEncryptString(new ObjectMapper().writeValueAsString(rootNode),cipherKey);
    }

    public String constructCipherOfVtoCVerify(TS ts) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        setTSNode(rootNode,ts);
        return DESUtil.getEncryptString(new ObjectMapper().writeValueAsString(rootNode),cipherKey);
    }

    // 邮件不需要再加密了
    /****
     * C to V EmaiL发送 加密加强邮件
     * @param email
     * @param emailID
     * @param emailKey
     * @return
     * @throws JsonProcessingException
     */
    public String constructCipherOfEmailSendInSafety(Email email, String emailID,String emailKey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        setEmailNode(rootNode,email,emailID,emailKey);
        return DESUtil.getEncryptString(new ObjectMapper().writeValueAsString(rootNode),cipherKey);
    }

    public String constructCipherOfEmailSend(Email email) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();



//        ObjectNode parentNode = jsonNodeFactory.objectNode();
//        parentNode.put("Id",email.getId());
//        setUser(parentNode,email.getSender());
//        setUser(parentNode,email.getReceiver());
//        parentNode.put("Title",email.getTitle());
//        parentNode.put("Time",email.getTime());
//        parentNode.put("Type",email.getType());
//        parentNode.put("Context",email.getContext());

        rootNode.set(Email.class.getSimpleName(),getEmailNode(email));
        //setEmailNode(rootNode,email);

        //rootNode.put("Email",getPackageEmailToGson(email));
        //rootNode.set("Email",parentNode);
        //rootNode.set("Email",setEmailNode();)
       // setEmailNode(rootNode,email);
        return DESUtil.getEncryptString(new ObjectMapper().writeValueAsString(rootNode),cipherKey);
    }


//    public String constructCipherOfEmailList(EmailList emailList) throws JsonProcessingException {
//        ObjectNode rootNode = jsonNodeFactory.objectNode();
//        rootNode.put(emailList.getClass().getSimpleName(),getPackageEmailListToGson(emailList));
//
////        ObjectMapper mapper = new ObjectMapper();
////        ArrayNode array = mapper.createArrayNode();
////        ObjectNode rootNode = jsonNodeFactory.objectNode();
////        for(int i=0;i<emailList.getListNumber();i++){
////            array.add(getPackageEmailToGson(emailList.getEmailAtIndex(i)) );
////        }
//        return DESUtil.getEncryptString(new ObjectMapper().writeValueAsString(rootNode),cipherKey);
//    }

    public String constructCipherOfEmailList(EmailList emailList) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        //rootNode.put(emailList.getClass().getSimpleName(),getPackageEmailListToGson(emailList));

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        //ObjectNode rootNode = jsonNodeFactory.objectNode();
        for(int i=0;i<emailList.getListNumber();i++){
            array.add(getEmailNode(emailList.getEmailAtIndex(i)) );
        }
        rootNode.set(emailList.getClass().getSimpleName(),array);
        return DESUtil.getEncryptString(new ObjectMapper().writeValueAsString(rootNode),cipherKey);
    }

    //FIXME
    public String constructCipherOfEmailList(EmailList sendList,EmailList receiveList) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();

        rootNode.set(sendList.getClass().getSimpleName(),getEmailListNode(sendList));
        rootNode.set(receiveList.getClass().getSimpleName(),getEmailListNode(receiveList));
       // rootNode.put(sendList.getClass().getSimpleName(),getPackageEmailListToGson(sendList));
       // rootNode.put(receiveList.getClass().getSimpleName(),getPackageEmailListToGson(receiveList));
        return DESUtil.getEncryptString(new ObjectMapper().writeValueAsString(rootNode),cipherKey);
    }

    public CipherConstructor(String cipherKey) {
        this.cipherKey=cipherKey;
    }
    public CipherConstructor() {
    }

    public String getCipherKey() {
        return cipherKey;
    }
}
