import org.graalvm.polyglot.*;


public class GetPID{

  public static Value getFunction(Context ctx){
    if(System.getProperty("os.name").startsWith("Windows")){
      return ctx.eval("nfiwrapper", "load 'Kernel32.dll'")
                .getMember("GetCurrentProcessId")
                .invokeMember("bind", "():UINT32");
    }
    else{
      return ctx.eval("nfiwrapper", "load 'libc.so.6'")
                .getMember("getpid")
                .invokeMember("bind", "():SINT32");
    }
  }

  public static void main(String[] args) throws Exception{
    try(var ctx = Context.newBuilder("nfiwrapper")
                         .allowNativeAccess(true)
                         .build()){
      var getpid = getFunction(ctx);
      int pid = getpid.execute().asInt();
      System.out.println("PID: " + pid);
    }
  }
}
