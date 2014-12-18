package com.radmation.RAnayaBattleship;

/**
 * Created by Radley on 11/9/2014.
 */
public class ServerRequest {
    String url;
    BaseActivity.ServerCommands command;
    Integer resultCode;
    String jsonDataResult;
    String errorString;

    // ServerRequest Constructor
    public ServerRequest( String _url, BaseActivity.ServerCommands _command ) {
        url = _url;
        command = _command;
        resultCode = null;
        jsonDataResult = null;
        errorString = null;
    }

    //Getter
    public String getUrl() {
        return url;
    }

    //Setter
    public void setUrl( String _url ) {
        url = _url;
    }

    //Getter
    public BaseActivity.ServerCommands getCommand() {
        return command;
    }

    //Setter
    public void setCommand( BaseActivity.ServerCommands _command ) {
        command = _command;
    }

    //Getter
    public Integer getResultCode() {
        return resultCode;
    }

    // Setter
    public void setResultCode( Integer _resultCode ) {
        resultCode = _resultCode;
    }

    //Getter
    public String getJsonDataResult() {
        return jsonDataResult;
    }

    //Setter
    public void setJsonDataResult( String _jsonDataResult ) {
        jsonDataResult = _jsonDataResult;
    }

    //Getter
    public String getErrorString() {
        return errorString;
    }

    //Setter
    public void setErrorString( String _errorString ) {
        errorString = _errorString;
    }

}