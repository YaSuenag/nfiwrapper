package com.yasuenag.nfiwrapper;

import com.oracle.truffle.api.*;


public class NFIWrapperContext{

  private TruffleLanguage.Env env;

  public NFIWrapperContext(TruffleLanguage.Env env){
    this.env = env;
  }

  public void setEnv(TruffleLanguage.Env env){
    this.env = env;
  }

  public TruffleLanguage.Env getEnv(){
    return env;
  }

}
