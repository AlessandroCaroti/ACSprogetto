package server;

import utility.ResponseCode;

public class ResponseCodeList {
    //SERVER ERROR: unexpected exception was encountered
    final static public  ResponseCode InternalError = new  ResponseCode(ResponseCode.Codici.R505, ResponseCode.TipoClasse.SERVER, "Internal Server Error =(");
    //CLIENT ERROR
    final static public  ResponseCode ClientError   = new ResponseCode(ResponseCode.Codici.R610, ResponseCode.TipoClasse.SERVER, "Registrazione account fallita!");
    final static public  ResponseCode LoginFailed   = new ResponseCode(ResponseCode.Codici.R630, ResponseCode.TipoClasse.SERVER, "Login Failed: The user name or password is incorrect!");

}
