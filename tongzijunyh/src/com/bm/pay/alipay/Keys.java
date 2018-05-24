package com.bm.pay.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字   2088911456538585
	public static final String DEFAULT_PARTNER = "2088511323648104";

	//收款支付宝账号
	public static final String DEFAULT_SELLER = "1902562799@qq.com";  

	//商户私钥，自助生成
	public static final String PRIVATE = "MIICXgIBAAKBgQDuO/obesAFWt9dK58A5LHCrPiBGMp6AdhKmNoFDR+E2FD8mSsTxJipQ1KUZE1Zy+6P/Wa2uMXLT0hFBujoA6SEnJeo6FcTtgvlWaXiObzCqBFSutBhrRHJk0b9+pxp0povH7gij8KhF/EMg449UHTgaVRXmrgjriw8U15eSK6GwwIDAQABAoGBAIuF2M6rLzZzqAk6+N5fEbR/9PI2gK6/0dqudlErBGCbB6NIWTA0nWvUH9MEg0Cp7MerkrJEuKJ/hkXW7CSlrX1Tq/qY6iXti/uzGqjaELCl3rbf0btAASey7gOWQTxtveLofoTiEWy+WbVx4dPjEBfW3grcDaA90qL7rh8f/6ahAkEA9zmvbaNOK0rBQqAQQii6e20kNyCs67tzN9h3uJUkbUQr2t986n0/nMxypQSrOnNb+qfL2+IUFhgFE/tqxN6DmwJBAPawmR8bj7SZ8OKmLPcFYxELxZ8mBWUQi+2phaERRW3pRwgPxTdq33mUSAwHvO1A7X0OC/cE7ytQwkHfqFveX/kCQAxkWtO/DMsEuz0wsr/uNLK9HMSaobBLPBI/ini5GRNVCHG3JBbI4mhkoxZ8bmzGnYSp5oEGCwW0fISs5IQrtyMCQQDCJ2OcNBj6T15jQ96H3FmIBPfYthfw3wECYDWn+uvmAX/CPIc/goG4Mpgxv7VWFY/UqMWJ+Pwz3y/EoL7k7zQhAkEApEHuxWu0mMlcnEqyC3l8vnGYRW6nU8qhX1CcShrT872FIH/JfHBh68JrLvafhG9Z/S9C+P6+xoKO+Fn0w6CNwg==";

	//公钥
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDuO/obesAFWt9dK58A5LHCrPiBGMp6AdhKmNoFDR+E2FD8mSsTxJipQ1KUZE1Zy+6P/Wa2uMXLT0hFBujoA6SEnJeo6FcTtgvlWaXiObzCqBFSutBhrRHJk0b9+pxp0povH7gij8KhF/EMg449UHTgaVRXmrgjriw8U15eSK6GwwIDAQAB";

}
