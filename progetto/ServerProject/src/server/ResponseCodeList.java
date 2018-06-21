package server;

import utility.ResponseCode;

class ResponseCodeList {
    //SERVER ERROR: unexpected exception was encountered
    final static   ResponseCode InternalError = new  ResponseCode(ResponseCode.Codici.R505, ResponseCode.TipoClasse.SERVER, "Internal Server Error =(");
    //CLIENT ERROR
    final static   ResponseCode ClientError   = new ResponseCode(ResponseCode.Codici.R610, ResponseCode.TipoClasse.SERVER, "Registrazione account fallita!");
    final static   ResponseCode LoginFailed   = new ResponseCode(ResponseCode.Codici.R630, ResponseCode.TipoClasse.SERVER, "Login Failed: The user name or password is incorrect!");
    final static ResponseCode WrongCodeValidation=new ResponseCode(ResponseCode.Codici.R640,ResponseCode.TipoClasse.SERVER,"The entered code is incorrect! Attempts terminated, repeat the procedure.");
    final static   ResponseCode InvalidUsername=new ResponseCode(ResponseCode.Codici.R200,ResponseCode.TipoClasse.SERVER,"Username already in use! Repeat the procedure.");


}
