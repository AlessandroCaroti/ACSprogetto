package server;

import utility.ResponseCode;

public class ResponseCodeList {
    final static public  ResponseCode InternalError = new  ResponseCode(ResponseCode.Codici.R505, ResponseCode.TipoClasse.SERVER, "Internal Server Error. =(");
    final static public  ResponseCode ClientError   = new ResponseCode(ResponseCode.Codici.R610, ResponseCode.TipoClasse.SERVER, "Registrazione account fallita!");
}
