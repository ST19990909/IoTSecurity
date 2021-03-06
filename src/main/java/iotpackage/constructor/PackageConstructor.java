package iotpackage.constructor;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import iotpackage.IPInfo;
import iotpackage.IoTKey;
import iotpackage.data.autheticator.Authenticator;
import iotpackage.data.ciphertext.Ciphertext;
import iotpackage.data.ciphertext.Lifetime;
import iotpackage.data.fuction.Email;
import iotpackage.data.fuction.User.User;
import iotpackage.data.fuction.emailList.EmailList;
import iotpackage.data.ticket.Ticket;
import iotpackage.destination.Destination;
import iotpackage.source.Source;

import iotpackage.data.*;
import securityalgorithm.DESUtil;
import securityalgorithm.MD5Util;
import securityalgorithm.RSAUtil;


public class PackageConstructor {

    //JsonNodeFactory 实例，可全局共享
    private JsonNodeFactory jsonNodeFactory;
    //JsonFactory 实例，线程安全
    private JsonFactory jsonFactory ;

    public PackageConstructor() {
        this.jsonNodeFactory = JsonNodeFactory.instance;
        this.jsonFactory  = new JsonFactory();
    }

    /**工具函数*/
    //source节点添加
    void setIPinfoNode(ObjectNode parentNode,IPInfo info){

        ObjectNode sourceNode = jsonNodeFactory.objectNode();
        sourceNode.put("Id",info.getId());
        sourceNode.put("IP",info.getIp());
        parentNode.set(info.getClass().getSimpleName(),sourceNode);
    }

    //source节点添加
    void setSourceNode(ObjectNode parentNode,Source source){

        ObjectNode sourceNode = jsonNodeFactory.objectNode();
        sourceNode.put("Id",source.getId());
        sourceNode.put("IP",source.getIp());
        parentNode.set("Source",sourceNode);
    }

    //destination节点添加
    void setDestionationNode(ObjectNode parentNode,Destination destination){
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
        ObjectNode lifetimeNode=jsonNodeFactory.objectNode();
        lifetimeNode.put("Id",lifetime.getId());
        lifetimeNode.put("Context",lifetime.getContext());
        parentNode.set("Lifetime",lifetimeNode);

    }

    //密文节点添加
    void setCipherNode(ObjectNode parentNode,Ciphertext ciphertext){
        ObjectNode ciphertextNode=jsonNodeFactory.objectNode();

        ciphertextNode.put("Context",ciphertext.getContext());
        ciphertextNode.put("Id",ciphertext.getId());
        parentNode.set("Ciphertext",ciphertextNode);

    }

    //Ticket节点添加
    //Ticket 保密v
    void setTicketNode(ObjectNode parentNode,String ticketContext,String ticketID){
        ObjectNode ticketNode=jsonNodeFactory.objectNode();
        ticketNode.put("Id",ticketID);
        ticketNode.put("Context",ticketContext);

        parentNode.set("Ticket",ticketNode);

    }

    //凭证节点添加
    // Authenticator 加密状态
    void setAuthenticatorNode(ObjectNode parentNode,String authenticatorContext,String authenticatorID){
        ObjectNode authenticatorNode=jsonNodeFactory.objectNode();
        authenticatorNode.put("Id",authenticatorID);
        authenticatorNode.put("Context", authenticatorContext);

        parentNode.set("Authenticator", authenticatorNode);
    }
    //Email邮件添加
    public void setEmailNode(ObjectNode parentNode,Email email) {
        //ObjectNode rootNode = jsonNodeFactory.objectNode();
        setUser(parentNode,email.getSender());
        setUser(parentNode,email.getReceiver());
        parentNode.put("Title",email.getTitle());
        parentNode.put("Time",email.getTime());
        parentNode.put("Type",email.getType());
        parentNode.put("Context",email.getContext());

        //setTSNode(rootNode,authenticator.getTs());*/
        //  return new ObjectMapper().writeValueAsString(rootNode);
    }

    //Ticket json格式生成
    /**
     * TGS /V
     * ***/
    public String getPackageTikectToGson(Ticket ticket) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        //rootNode.put("Id",tgs.getId())
        setKeyNode(rootNode,ticket.getKey());
        setSourceNode(rootNode,ticket.getId());
        setDestionationNode(rootNode,ticket.getAd());
        setTSNode(rootNode,ticket.getTs());
        setLifetimeNode(rootNode,ticket.getLifetime());
        //ObjectMapper objectMapper = new ObjectMapper();

        //String signContext= objectMapper.writeValueAsString(rootNode);
        return new ObjectMapper().writeValueAsString(rootNode);
    }

    //邮件
    public void setUser(ObjectNode parentNode, User user){
        ObjectNode userNode=jsonNodeFactory.objectNode();
        userNode.put("Account",user.getAccount());
        userNode.put("Nickname",user.getNickname());
        parentNode.set(user.getClass().getSimpleName(),userNode);
    }

    public String getPackageEmailToGson(Email email) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        setUser(rootNode,email.getSender());
        setUser(rootNode,email.getReceiver());
        rootNode.put("Title",email.getTitle());
        rootNode.put("Time",email.getTime());
        rootNode.put("Type",email.getType());
        rootNode.put("Context",email.getContext());

        //setTSNode(rootNode,authenticator.getTs());*/
        return new ObjectMapper().writeValueAsString(rootNode);
    }


//    /***
//     * AS to C
//     * 登入响应，并带上nickname
//     * @param process 进程代号
//     * @param operation 操作代号
//     * @param source 发送方
//     * @param destination 接受方
//     * @param code 操作码
//     * @param publicKey 验证公钥
//     * */
//    public String getPackageServiceResponse(String process, String operation, Source source, Destination destination, String code,String nickname,String privateKey,String publicKey) throws JsonProcessingException {
//        ObjectNode rootNode = jsonNodeFactory.objectNode();
//        ObjectNode infoNode = jsonNodeFactory.objectNode();
//        ObjectNode signNode = jsonNodeFactory.objectNode();
//        infoNode.put("Process",process);
//        infoNode.put("Operation",operation);
//
//        //source节点添加
//        setSourceNode(infoNode,source);
//
//        //destination节点添加
//        setDestionationNode(infoNode,destination);
//
//        //Data字段
//        ObjectNode dataNode = jsonNodeFactory.objectNode();
//        dataNode.put("Code",code);
//        dataNode.put("Nickname",nickname);
//
//
//        infoNode.set("Data",dataNode);
//        rootNode.set("Info",infoNode);
//
//
//        if(publicKey.length()==0){
//            signNode.put("Context","");
//
//        }else{
//            //签名是加密
//            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,privateKey);
//            signNode.put("Context",signContext);
//        }
//
//        signNode.put("PublicKey",publicKey);
//        rootNode.set("Sign",signNode);
//        // ObjectMapper objectMapper = new ObjectMapper();
//        return new ObjectMapper().writeValueAsString(rootNode);
//    }

    /***
     * AS to C
     * 登入响应，并带上nickname
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param publicKey 验证公钥
     * */
    public String getPackageServiceResponse(String process, String operation, IPInfo source, IPInfo destination, String code, String nickname, String privateKey, String publicKey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();
        ObjectNode signNode = jsonNodeFactory.objectNode();
        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setIPinfoNode(infoNode,source);

        //destination节点添加
        setIPinfoNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        dataNode.put("Nickname",nickname);


        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        if(publicKey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,privateKey);
            signNode.put("Context",signContext);
        }

        signNode.put("PublicKey",publicKey);
        rootNode.set("Sign",signNode);
        // ObjectMapper objectMapper = new ObjectMapper();
        return new ObjectMapper().writeValueAsString(rootNode);
    }

    @Deprecated
    /***
     * 服务器响应代码
     * service to C
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param publickey 验证公钥
     * */
    public String getPackageServiceResponse(String process, String operation, Source source, Destination destination, String code,String publickey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();
        ObjectNode signNode = jsonNodeFactory.objectNode();
        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);


        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        if(publickey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,publickey);
            signNode.put("Context",signContext);
        }

        signNode.put("PublicKey",publickey);
        rootNode.set("Sign",signNode);
        // ObjectMapper objectMapper = new ObjectMapper();
        return new ObjectMapper().writeValueAsString(rootNode);
    }

    /***
     * service to C 服务器响应代码
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param privateKey 签名私钥
     * @param publicKey 验证公钥
     * @return 生成的json串
     * */
    public String getPackageServiceResponse(String process, String operation,
                                            Source source, Destination destination,
                                            String code,
                                            String privateKey,String publicKey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();
        ObjectNode signNode = jsonNodeFactory.objectNode();
        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);


        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        if(publicKey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,privateKey);
            signNode.put("Context",signContext);
        }

        signNode.put("PublicKey",publicKey);
        rootNode.set("Sign",signNode);
        // ObjectMapper objectMapper = new ObjectMapper();
        return new ObjectMapper().writeValueAsString(rootNode);
    }

    @Deprecated
    /**用户注册
     * C to AS
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param account 用户账户
     * @param password 密码
     *
     * @param nickname 昵称
     * @param securityQuestion 安全问题
     * @param securityAnswer 安全问题的答案
     * ****/
    public String getPackageCtoASRegist(String process, String operation,
                                        Source source, Destination destination,
                                        String code,
                                        String account,String password,
                                        String nickname,String securityQuestion,
                                        String securityAnswer,String publickey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();
        ObjectNode signNode = jsonNodeFactory.objectNode();
        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        dataNode.put("Account",account);
        // String passwordEn= DESUtil.getEncryptString(password,deskey);
        dataNode.put("Password", MD5Util.md5(password));
        dataNode.put("Nickname",nickname);
        dataNode.put("SecurityQuestion",securityQuestion);
        dataNode.put("SecurityAnswer",securityAnswer);

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);

        if(publickey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,publickey);
            signNode.put("Context",signContext);
        }

        signNode.put("PublicKey",publickey);
        rootNode.set("Sign",signNode);
        // ObjectMapper objectMapper = new ObjectMapper();
        return new ObjectMapper().writeValueAsString(rootNode);
    }


    /***
     * 用户注册
     * C to AS
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param account 用户账户
     * @param password 密码
     * @param nickname 昵称
     * @param securityQuestion 安全问题
     * @param securityAnswer 安全问题的答案
     * @param privateKey 签名私钥
     * @param publicKey 验证公钥
     * @return 生成的json
     * ****/
    public String getPackageCtoASRegist(String process, String operation,
                                        Source source, Destination destination,
                                        String code,
                                        String account,String password,
                                        String nickname,String securityQuestion,
                                        String securityAnswer,
                                        String privateKey,String publicKey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();
        ObjectNode signNode = jsonNodeFactory.objectNode();
        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        dataNode.put("Account",account);
        dataNode.put("Password", MD5Util.md5(password));
        dataNode.put("Nickname",nickname);
        dataNode.put("SecurityQuestion",securityQuestion);
        dataNode.put("SecurityAnswer",securityAnswer);

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);

        if(publicKey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,privateKey);
            signNode.put("Context",signContext);
        }

        signNode.put("PublicKey",publicKey);
        rootNode.set("Sign",signNode);
        // ObjectMapper objectMapper = new ObjectMapper();
        return new ObjectMapper().writeValueAsString(rootNode);
    }

    /***服务认证阶段***/
    @Deprecated
    /***
     * C to AS
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param idC 用户IP地址
     * @param idTGS TGS的IP地址
     * @param ts 为时间戳 ,如2020-6-15 10:00:00
     * ****/
    public String  getPackageCtoASLogin(String process, String operation,
                                        Source source, Destination destination,
                                        String code,
                                        String idC, String idTGS, TS ts,
                                        String publickey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();
        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        dataNode.put("IdC",idC);
        dataNode.put("IdTGS",idTGS);
        //时间戳节点添加
        setTSNode(dataNode,ts);

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);

        //sign签名
        if(publickey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,publickey);
            signNode.put("Context",signContext);
        }


        // String signResult=signContext;
        //

        signNode.put("PublicKey",publickey);

        rootNode.set("Sign",signNode);

        // rootNode.put("1","2");



        return new ObjectMapper().writeValueAsString(rootNode);
        //  String result = "";
        /*JsonGenerator generator =
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeTree(generator, rootNode);*/
    }

    /****
     * AS验证阶段
     * C to AS
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param idC 用户IP地址
     * @param idTGS TGS的IP地址
     * @param ts 为时间戳 ,如2020-6-15 10:00:00
     * @param privateKey 加密私钥
     * @param publickey 验证公钥
     * @return 生成的json
     * @throws JsonProcessingException jsons 生成异常
     */
    public String  getPackageCtoASLogin(String process, String operation,
                                        Source source, Destination destination,
                                        String code,
                                        String idC, String idTGS, TS ts,String privateKey,
                                        String publickey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();
        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        dataNode.put("IdC",idC);
        dataNode.put("IdTGS",idTGS);
        //时间戳节点添加
        setTSNode(dataNode,ts);

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);

        //sign签名
        if(publickey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,privateKey);
            signNode.put("Context",signContext);
        }

        signNode.put("PublicKey",publickey);

        rootNode.set("Sign",signNode);


        return new ObjectMapper().writeValueAsString(rootNode);
    }

    @Deprecated
    /**
      AS to C
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param idC 用户的ip地址
     * @param cipherKey 加密密文的密钥 为用户的password的md5的值
     * @param CandTGS 由AS生成的C和TGS交互的密钥，ticket tgs中有一份
     * @param idTGS tgs的ip地址
     * @param ts 时间戳
     * @param lifetime 有效时间
     * @param tgs tgs的票据
     * @param ticketKey 加密ticket的票据的key
     * @param publickey 签名公钥
     * ****/
    public String  getPackageAStoCLogin(String process, String operation,
                                        Source source, Destination destination,
                                        String code,
                                        String idC,
                                        String cipherKey,
                                        IoTKey CandTGS, String idTGS, TS ts, Lifetime lifetime,
                                        Ticket tgs,
                                        String ticketKey,
                                        String publickey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        dataNode.put("IdC",idC);
        dataNode.put("IdTGS",idTGS);
        //时间戳节点添加
        setTSNode(dataNode,ts);

        CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfAStoC(CandTGS,idTGS,ts,lifetime,tgs,ticketKey),"AStoCLoginResponse"));

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        ObjectMapper objectMapper = new ObjectMapper();

        //签名算法
        if(publickey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,publickey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publickey);

        rootNode.set("Sign",signNode);

        // rootNode.put("1","2");



        return objectMapper.writeValueAsString(rootNode);
    }

    @Deprecated
    /***AS to C
     *
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param idC 用户的ip地址
     * @param cipherKey 加密密文的密钥 为用户的password的md5的值
     * @param CandTGS 由AS生成的C和TGS交互的密钥，ticket tgs中有一份
     * @param idTGS tgs的ip地址
     * @param ts 时间戳
     * @param lifetime 有效时间
     * @param tgs tgs的票据
     * @param ticketID tgs的ID
     * @param ticketKey 加密ticket的票据的key
     * @param publickey 签名公钥
     * ****/
    public String  getPackageAStoCLogin(String process, String operation,
                                        Source source, Destination destination,
                                        String code,
                                        String idC,
                                        String cipherKey,
                                        IoTKey CandTGS, String idTGS, TS ts, Lifetime lifetime,
                                        Ticket tgs,
                                        String ticketID,
                                        String ticketKey,
                                        String publickey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        dataNode.put("IdC",idC);
        dataNode.put("IdTGS",idTGS);
        //时间戳节点添加
        setTSNode(dataNode,ts);

        CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfAStoC(CandTGS,idTGS,ts,lifetime,tgs,ticketID,ticketKey),"AStoCLoginResponse"));

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        ObjectMapper objectMapper = new ObjectMapper();

        //签名算法
        if(publickey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,publickey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publickey);

        rootNode.set("Sign",signNode);

        // rootNode.put("1","2");



        return objectMapper.writeValueAsString(rootNode);
    }

    /***AS to C
     *
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param idC 用户的ip地址
     * @param cipherKey 加密密文的密钥 为用户的password的md5的值
     * @param CandTGS 由AS生成的C和TGS交互的密钥，ticket tgs中有一份
     * @param idTGS tgs的ip地址
     * @param ts 时间戳
     * @param lifetime 有效时间
     * @param tgs tgs的票据
     * @param ticketID tgs的ID
     * @param ticketKey 加密ticket的票据的key
     * @param privateKey 加密私钥
     * @param publicKey 验证公钥
     * ****/
    public String  getPackageAStoCLogin(String process, String operation,
                                        Source source, Destination destination,
                                        String code,
                                        String idC,
                                        String cipherKey,
                                        IoTKey CandTGS, String idTGS, TS ts, Lifetime lifetime,
                                        Ticket tgs,
                                        String ticketID,
                                        String ticketKey,
                                        String privateKey,String publicKey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        dataNode.put("IdC",idC);
        dataNode.put("IdTGS",idTGS);
        //时间戳节点添加
        setTSNode(dataNode,ts);

        CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfAStoC(CandTGS,idTGS,ts,lifetime,tgs,ticketID,ticketKey),"AStoCLoginResponse"));

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        ObjectMapper objectMapper = new ObjectMapper();

        //签名算法
        if(publicKey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,privateKey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publicKey);

        rootNode.set("Sign",signNode);

        // rootNode.put("1","2");



        return objectMapper.writeValueAsString(rootNode);
    }

    @Deprecated
    /***C to TGS
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param idV 用户的访问的V的IP
     * @param tgsContext tgs的票据
     * @param tgsticketID ticket ID号
     * @param authenticator 有效证明
     * @param authenticatorID 有效证明id
     * @param authenticator  用户授权
     * @param publickey 签名公钥
     * ****/
    public String  getPackageCtoTGS(String process, String operation,
                                    Source source, Destination destination,
                                    String code,
                                    String idV,
                                    String tgsContext,
                                    String tgsticketID,
                                    Authenticator authenticator,
                                    String authenticatorKey,
                                    String authenticatorID,
                                    String publickey
    ) throws JsonProcessingException {

        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        dataNode.put("IdV",idV);
        //setTicket
        setTicketNode(dataNode,tgsContext,tgsticketID);
        setAuthenticatorNode(dataNode,DESUtil.getEncryptString(
                new CipherConstructor().getPackageAuthenticatorToGson(authenticator),authenticatorKey),
                authenticatorID);

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        //签名算法
        if(publickey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,publickey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publickey);


        rootNode.set("Sign",signNode);
        return new ObjectMapper().writeValueAsString(rootNode);
    }



    /***C to TGS
     *
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param idV 用户的访问的V的IP
     * @param tgsContext tgs的票据
     * @param tgsticketID ticket ID号
     * @param authenticatorID 有效证明id
     * @param authenticator  用户授权
     * @param privateKey 加密私钥
     * @param publicKey 签名公钥
     * @return 生成的json
     * @throws  JsonProcessingException json生成失败
     * ****/
    public String  getPackageCtoTGS(String process, String operation,
                                    Source source, Destination destination,
                                    String code,
                                    String idV,
                                    String tgsContext,
                                    String tgsticketID,
                                    Authenticator authenticator,
                                    String authenticatorKey,
                                    String authenticatorID,
                                    String privateKey,String publicKey
    ) throws JsonProcessingException {

        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        dataNode.put("IdV",idV);
        //setTicket
        setTicketNode(dataNode,tgsContext,tgsticketID);
        setAuthenticatorNode(dataNode,DESUtil.getEncryptString(
                new CipherConstructor().getPackageAuthenticatorToGson(authenticator),authenticatorKey),
                authenticatorID);

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        //签名算法
        if(publicKey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,privateKey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publicKey);


        rootNode.set("Sign",signNode);
        return new ObjectMapper().writeValueAsString(rootNode);
    }

    @Deprecated
    /***
     * AS to C
     *
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param cipherKey 加密密文的V密钥 AS生成的
     * @param CandV 由TGS生成的C和V交互的密钥，ticketV有一份
     * @param idV V的ip地址
     * @param ts 时间戳 代号为4
     * @param ticketV tgs的票据
     * @param ticketID tgs的ID
     * @param ticketKey 加密ticket的票据的key
     * @param publickey 签名公钥
     * ****/
    public String  getPackageTGStoC(String process, String operation,
                                    Source source, Destination destination,
                                    String code,
                                    String cipherKey,
                                    IoTKey CandV, String idV, TS ts,
                                    Ticket ticketV,
                                    String ticketID,
                                    String ticketKey,
                                    String publickey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        //时间戳节点添加
        setTSNode(dataNode,ts);

        CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfTGStoC(CandV,idV,ts,ticketV,ticketID,ticketKey),"TGStoC"));

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        ObjectMapper objectMapper = new ObjectMapper();

        //签名算法
        if(publickey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,publickey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publickey);

        rootNode.set("Sign",signNode);

        return objectMapper.writeValueAsString(rootNode);
    }

    /***AS to C
     *
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param cipherKey 加密密文的V密钥 AS生成的
     * @param CandV 由TGS生成的C和V交互的密钥，ticketV有一份
     * @param idV V的ip地址
     * @param ts 时间戳 代号为4
     * @param ticketV tgs的票据
     * @param ticketID tgs的ID
     * @param ticketKey 加密ticket的票据的key
     * @param privateKey 加密私钥
     * @param publicKey 签名公钥
     * @return 生成的json
     * @throws JsonProcessingException json生成失败
     * ****/
    public String  getPackageTGStoC(String process, String operation,
                                    Source source, Destination destination,
                                    String code,
                                    String cipherKey,
                                    IoTKey CandV, String idV, TS ts,
                                    Ticket ticketV,
                                    String ticketID,
                                    String ticketKey,
                                    String privateKey,String publicKey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        //时间戳节点添加
        setTSNode(dataNode,ts);

        CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfTGStoC(CandV,idV,ts,ticketV,ticketID,ticketKey),"TGStoC"));

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        ObjectMapper objectMapper = new ObjectMapper();

        //签名算法
        if(publicKey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,privateKey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publicKey);

        rootNode.set("Sign",signNode);

        return objectMapper.writeValueAsString(rootNode);
    }


    @Deprecated
    public String getPackageCtoVVerify(String process, String operation,
                                       Source source, Destination destination,
                                       String code,
                                       String ticketV,String ticketID,
                                       Authenticator authenticator,String authenticatorKey, String authenticatorID, String publickey) throws JsonProcessingException {
        ObjectNode rootNode = this.jsonNodeFactory.objectNode();
        ObjectNode infoNode = this.jsonNodeFactory.objectNode();
        ObjectNode signNode = this.jsonNodeFactory.objectNode();
        infoNode.put("Process", process);
        infoNode.put("Operation", operation);
        this.setSourceNode(infoNode, source);
        this.setDestionationNode(infoNode, destination);
        ObjectNode dataNode = this.jsonNodeFactory.objectNode();
        dataNode.put("Code", code);
        this.setTicketNode(dataNode,ticketV,ticketID);
        this.setAuthenticatorNode(dataNode, DESUtil.getEncryptString((new CipherConstructor()).getPackageAuthenticatorToGson(authenticator), authenticatorKey), authenticatorID);
        infoNode.set("Data", dataNode);
        rootNode.set("Info", infoNode);

        if (publickey.length() == 0) {
            signNode.put("Context", "");
        } else {
            String signContext = RSAUtil.privateEncrypt(MD5Util.md5((new ObjectMapper()).writeValueAsString(infoNode)), publickey);
            signNode.put("Context", signContext);
        }

        signNode.put("PublicKey", publickey);
        rootNode.set("Sign", signNode);
        //printJason(rootNode);
        return (new ObjectMapper()).writeValueAsString(rootNode);
    }

    /****
     * C to V 验证阶段
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param ticketV V的票据
     * @param ticketID 票据ID
     * @param authenticator 授权
     * @param authenticatorKey 授权加密密钥
     * @param authenticatorID 授权ID
     * @param privateKey 加密私钥
     * @param publicKey 验证公钥
     * @return 生成json
     * @throws JsonProcessingException 生成的json失败
     */
    public String getPackageCtoVVerify(String process, String operation,
                                       Source source, Destination destination,
                                       String code,
                                       String ticketV,String ticketID,
                                       Authenticator authenticator,String authenticatorKey, String authenticatorID,
                                       String privateKey,String publicKey) throws JsonProcessingException {
        ObjectNode rootNode = this.jsonNodeFactory.objectNode();
        ObjectNode infoNode = this.jsonNodeFactory.objectNode();
        ObjectNode signNode = this.jsonNodeFactory.objectNode();
        infoNode.put("Process", process);
        infoNode.put("Operation", operation);
        this.setSourceNode(infoNode, source);
        this.setDestionationNode(infoNode, destination);
        ObjectNode dataNode = this.jsonNodeFactory.objectNode();
        dataNode.put("Code", code);
        this.setTicketNode(dataNode,ticketV,ticketID);
        this.setAuthenticatorNode(dataNode, DESUtil.getEncryptString((new CipherConstructor()).getPackageAuthenticatorToGson(authenticator), authenticatorKey), authenticatorID);
        infoNode.set("Data", dataNode);
        rootNode.set("Info", infoNode);

        if (publicKey.length() == 0) {
            signNode.put("Context", "");
        } else {
            String signContext = RSAUtil.privateEncrypt(MD5Util.md5((new ObjectMapper()).writeValueAsString(infoNode)), privateKey);
            signNode.put("Context", signContext);
        }

        signNode.put("PublicKey", publicKey);
        rootNode.set("Sign", signNode);
        //printJason(rootNode);
        return (new ObjectMapper()).writeValueAsString(rootNode);
    }

    @Deprecated
    /***
     * V to C Verify
     * @param process
     * @param operation
     * @param source
     * @param destination
     * @param code
     * @param cipherKey
     * @param publickey
     * @return
     * @throws JsonProcessingException
     */
    public String getPackageVtoCVerify(String process, String operation, Source source, Destination destination, String code,
                                       String cipherKey,
                                       TS ts,String publickey) throws JsonProcessingException {
        ObjectNode rootNode = this.jsonNodeFactory.objectNode();
        ObjectNode infoNode = this.jsonNodeFactory.objectNode();
        ObjectNode signNode = this.jsonNodeFactory.objectNode();
        infoNode.put("Process", process);
        infoNode.put("Operation", operation);
        this.setSourceNode(infoNode, source);
        this.setDestionationNode(infoNode, destination);
        ObjectNode dataNode = this.jsonNodeFactory.objectNode();
        dataNode.put("Code", code);

        CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfVtoCVerify(ts),"VtoCVerify"));

        infoNode.set("Data", dataNode);
        rootNode.set("Info", infoNode);
        if (publickey.length() == 0) {
            signNode.put("Context", "");
        } else {
            String signContext = RSAUtil.privateEncrypt(MD5Util.md5((new ObjectMapper()).writeValueAsString(infoNode)), publickey);
            signNode.put("Context", signContext);
        }

        signNode.put("PublicKey", publickey);
        rootNode.set("Sign", signNode);
        //printJason(rootNode);
        return (new ObjectMapper()).writeValueAsString(rootNode);
    }

    /***
     * V to C Verify
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param cipherKey 加密密文用的密钥
     * @param privateKey 加密私钥
     * @param publicKey 验证公钥
     * @return 生成的json
     * @throws JsonProcessingException json生成失败
     */
    public String getPackageVtoCVerify(String process, String operation, Source source, Destination destination, String code,
                                       String cipherKey,
                                       TS ts,
                                       String privateKey,String publicKey) throws JsonProcessingException {
        ObjectNode rootNode = this.jsonNodeFactory.objectNode();
        ObjectNode infoNode = this.jsonNodeFactory.objectNode();
        ObjectNode signNode = this.jsonNodeFactory.objectNode();
        infoNode.put("Process", process);
        infoNode.put("Operation", operation);
        this.setSourceNode(infoNode, source);
        this.setDestionationNode(infoNode, destination);
        ObjectNode dataNode = this.jsonNodeFactory.objectNode();
        dataNode.put("Code", code);

        CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfVtoCVerify(ts),"VtoCVerify"));

        infoNode.set("Data", dataNode);
        rootNode.set("Info", infoNode);
        if (publicKey.length() == 0) {
            signNode.put("Context", "");
        } else {
            String signContext = RSAUtil.privateEncrypt(MD5Util.md5((new ObjectMapper()).writeValueAsString(infoNode)), privateKey);
            signNode.put("Context", signContext);
        }

        signNode.put("PublicKey", publicKey);
        rootNode.set("Sign", signNode);
        //printJason(rootNode);
        return (new ObjectMapper()).writeValueAsString(rootNode);
    }

    @Deprecated
    /**
     *  C to V 和V to C都是这个，发送加密的邮件
     *邮件发送
     * @param process
     * @param operation
     * @param source
     * @param destination
     * @param code
     * @param cipherKey
     * @param email
     * @param publickey
     * @return
     * @throws JsonProcessingException
     */
    public String  getPackageEmailSend(String process, String operation,
                                       Source source, Destination destination,
                                       String code,
                                       String cipherKey,
                                       Email email,
                                       String publickey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        //时间戳节点添加

        CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfEmailSend(email),"C to V EmailSend"));

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        ObjectMapper objectMapper = new ObjectMapper();

        //签名算法
        if(publickey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,publickey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publickey);

        rootNode.set("Sign",signNode);

        return objectMapper.writeValueAsString(rootNode);
    }

    /*** C to V 和V to C都是这个，发送加密的邮件
     *邮件发送
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param cipherKey 加密密文
     * @param email 发送邮件
     * @param privateKey 验证私钥
     * @param publicKey 加密公钥
     * @return 生成的json
     * @throws JsonProcessingException json生成失败
     */
    public String  getPackageEmailSend(String process, String operation,
                                       Source source, Destination destination,
                                       String code,
                                       String cipherKey,
                                       Email email,
                                       String privateKey,String publicKey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();
        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        //时间戳节点添加

        CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfEmailSend(email),source.getId()+" to "+destination.getId()+" EmailSend"));

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        ObjectMapper objectMapper = new ObjectMapper();

        //签名算法
        if(publicKey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,privateKey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publicKey);

        rootNode.set("Sign",signNode);

        return objectMapper.writeValueAsString(rootNode);
    }

    @Deprecated
    /***
     * C to V 和V to C都是这个，发送指定的邮件
     *邮件发送
     * @param process
     * @param operation
     * @param source
     * @param destination
     * @param code
     * @param cipherKey
     * @param email
     * @param emailID
     * @param emailKey
     * @param publickey
     * @return
     * @throws JsonProcessingException
     */
    public String  getPackageEmailSendInSafety(String process, String operation,
                                               Source source, Destination destination,
                                               String code,
                                               String cipherKey,
                                               Email email,
                                               String emailID,
                                               String emailKey,
                                               String publickey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        //时间戳节点添加

        CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfEmailSendInSafety(email,emailID,emailKey),"TGS to C EmailSend in Safety"));

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        ObjectMapper objectMapper = new ObjectMapper();

        //签名算法
        if(publickey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,publickey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publickey);

        rootNode.set("Sign",signNode);

        return objectMapper.writeValueAsString(rootNode);
    }

    /*** C to V 和V to C都是这个，发送指定的邮件
     *邮件发送
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param cipherKey 加密密钥
     * @param email 发送邮件
     * @param emailID 邮件ID
     * @param emailKey　加密邮件的密钥
     * @param privateKey 验证私钥
     * @param publicKey 加密公钥
     * @return 生成的json
     * @throws JsonProcessingException 生成的json失败
     */
    public String  getPackageEmailSendInSafety(String process, String operation,
                                               Source source, Destination destination,
                                               String code,
                                               String cipherKey,
                                               Email email,
                                               String emailID,
                                               String emailKey,
                                               String privateKey,String publicKey) throws JsonProcessingException {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        //时间戳节点添加

        CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfEmailSendInSafety(email,emailID,emailKey),"TGS to C EmailSend in Safety"));

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        ObjectMapper objectMapper = new ObjectMapper();

        //签名算法
        if(publicKey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,privateKey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publicKey);

        rootNode.set("Sign",signNode);

        return objectMapper.writeValueAsString(rootNode);
    }

    @Deprecated
    /***
     * 邮件列表请求
     * @param process
     * @param operation
     * @param source
     * @param destination
     * @param code
     * @param account
     * @param publickey
     * @return
     * @throws JsonProcessingException
     */
    public String getPackageEmailCheck(String process, String operation,
                                       Source source, Destination destination,
                                       String code,
                                       String account,
                                       String publickey) throws JsonProcessingException {

        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        //时间戳节点添加

        dataNode.put("Account",account);
        //CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        //setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfEmailSend(email,emailID,emailKey),"TGStoC EmailSend"));

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        ObjectMapper objectMapper = new ObjectMapper();

        //签名算法
        if(publickey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,publickey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publickey);

        rootNode.set("Sign",signNode);

        return objectMapper.writeValueAsString(rootNode);
    }

    /***
     * 邮件列表请求
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param account　用户账户
     * @param privateKey 加密私钥
     * @param publicKey 验证公钥
     * @return 生成的json
     * @throws JsonProcessingException 生成json失败
     */

    public String getPackagePWDChange(String process, String operation,
                                       Source source, Destination destination,
                                       String code,
                                       String account,
                                       String NewPWD,
                                       String privateKey,String publicKey) throws JsonProcessingException {

        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        //时间戳节点添加

        dataNode.put("Account",account);
        dataNode.put("NewPWD",NewPWD);
        //CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        //setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfEmailSend(email,emailID,emailKey),"TGStoC EmailSend"));

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        ObjectMapper objectMapper = new ObjectMapper();

        //签名算法
        if(publicKey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,privateKey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publicKey);

        rootNode.set("Sign",signNode);

        return objectMapper.writeValueAsString(rootNode);
    }


    public String getPackageEmailCheck(String process, String operation,
                                       Source source, Destination destination,
                                       String code,
                                       String account,
                                       String privateKey,String publicKey) throws JsonProcessingException {

        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        //时间戳节点添加

        dataNode.put("Account",account);
        //CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        //setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfEmailSend(email,emailID,emailKey),"TGStoC EmailSend"));

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        ObjectMapper objectMapper = new ObjectMapper();

        //签名算法
        if(publicKey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,privateKey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publicKey);

        rootNode.set("Sign",signNode);

        return objectMapper.writeValueAsString(rootNode);
    }


    @Deprecated
    /**
     * 生成查询报文，
     * @param process
     * @param operation
     * @param source
     * @param destination
     * @param code
     * @param cipherKey
     * @param sendList
     * @param receiveList
     * @param publickey
     * @return
     * @throws JsonProcessingException
     */
    public String getPackageEmailListALL(String process, String operation,
                                         Source source, Destination destination,
                                         String code,
                                         String cipherKey,
                                         EmailList sendList,EmailList receiveList,
                                         String publickey) throws JsonProcessingException {

        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        //时间戳节点添加

        // dataNode.put("Account",account);
        CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfEmailList(sendList,receiveList),"V to "+destination.getId()+" EmailListAll"));

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        ObjectMapper objectMapper = new ObjectMapper();

        //签名算法
        if(publickey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,publickey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publickey);

        rootNode.set("Sign",signNode);

        return objectMapper.writeValueAsString(rootNode);
    }

    /****
     * 生成查询报文，
     * @param process 进程代号
     * @param operation 操作代号
     * @param source 发送方
     * @param destination 接受方
     * @param code 操作码
     * @param cipherKey　加密密文
     * @param sendList　发送箱
     * @param receiveList　收件箱
     * @param privateKey 加密私钥
     * @param publicKey 验证公钥
     * @return 　生成的json
     * @throws JsonProcessingException jsons生成失败
     */
    public String getPackageEmailListALL(String process, String operation,
                                         Source source, Destination destination,
                                         String code,
                                         String cipherKey,
                                         EmailList sendList,EmailList receiveList,
                                         String privateKey,String publicKey) throws JsonProcessingException {

        ObjectNode rootNode = jsonNodeFactory.objectNode();
        ObjectNode infoNode = jsonNodeFactory.objectNode();

        ObjectNode signNode = jsonNodeFactory.objectNode();

        infoNode.put("Process",process);
        infoNode.put("Operation",operation);

        //source节点添加
        setSourceNode(infoNode,source);

        //destination节点添加
        setDestionationNode(infoNode,destination);

        //Data字段
        ObjectNode dataNode = jsonNodeFactory.objectNode();
        dataNode.put("Code",code);
        //时间戳节点添加

        // dataNode.put("Account",account);
        CipherConstructor cipherConstructor=new CipherConstructor(cipherKey);
        setCipherNode(dataNode,new Ciphertext(cipherConstructor.constructCipherOfEmailList(sendList,receiveList),"V to "+destination.getId()+" EmailListAll"));

        infoNode.set("Data",dataNode);
        rootNode.set("Info",infoNode);


        //sign签名
        ObjectMapper objectMapper = new ObjectMapper();

        //签名算法
        if(publicKey.length()==0){
            signNode.put("Context","");

        }else{
            //签名是加密
            String signContext= RSAUtil.privateEncrypt(MD5Util.md5(new ObjectMapper().writeValueAsString(infoNode)) ,privateKey);
            signNode.put("Context",signContext);
        }
        signNode.put("PublicKey",publicKey);

        rootNode.set("Sign",signNode);

        return objectMapper.writeValueAsString(rootNode);
    }

}



