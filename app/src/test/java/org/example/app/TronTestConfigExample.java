package org.example.app;

/**
 * TRON 测试配置示例
 * 复制这个文件的内容到 TronTestConfig.java 并修改相应值
 */
public class TronTestConfigExample {
    
    // ============== 示例配置 ==============
    
    // 示例私钥 (请替换为你的实际私钥)
    public static final String SENDER_PRIVATE_KEY = "a1b2c3d4e5f6...your-64-character-private-key-here";
    
    // 示例发送方地址 (必须与私钥对应)
    public static final String FROM_ADDRESS = "TYourSenderAddressHere1234567890";
    
    // 示例接收方地址
    public static final String TO_ADDRESS = "TYourReceiverAddressHere1234567890";
    
    // ============== 常用配置选项 ==============
    
    // 网络选择示例：
    // "LOCAL"   - 本地节点
    // "NILE"    - Nile测试网
    // "SHASTA"  - Shasta测试网  
    // "MAINNET" - 主网
    public static final String NETWORK = "NILE"; // 推荐测试时使用
    
    // 转账金额示例：
    // 100,000 SUN = 0.1 TRX
    // 1,000,000 SUN = 1 TRX
    // 10,000,000 SUN = 10 TRX
    public static final long TRANSFER_AMOUNT = 100_000L; // 0.1 TRX (测试推荐)
    
    // 安全设置：测试时建议设为 false
    public static final boolean ACTUALLY_BROADCAST = false;
    
    // TronGrid API 密钥 (仅主网需要)
    public static final String TRONGRID_API_KEY = "your-api-key-from-trongrid-io";
    
    // ============== 获取测试资源 ==============
    
    /*
     * 如何获取测试用的 TRX：
     * 
     * 1. Nile 测试网水龙头:
     *    https://nileex.io/join/getJoinPage
     * 
     * 2. Shasta 测试网水龙头:
     *    https://www.trongrid.io/shasta
     * 
     * 3. 创建测试账户:
     *    使用 TronLink 或其他钱包创建新账户
     *    导出私钥和地址
     */
    
    // ============== 地址格式说明 ==============
    
    /*
     * TRON 地址格式：
     * - 以 'T' 开头
     * - 长度为 34 字符
     * - 示例: TLyqzVGLV1srkB7dToTAEqgDSfPtXRJZYH
     * 
     * 私钥格式：
     * - 64位十六进制字符串
     * - 示例: a1b2c3d4e5f6789012345678901234567890abcdef1234567890abcdef123456
     */
}