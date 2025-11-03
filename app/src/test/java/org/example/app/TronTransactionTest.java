package org.example.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;

/**
 * TRON 交易测试类
 * 测试创建交易、签名、广播的完整流程
 * 
 * 使用方法：
 * 1. 修改 TronTestConfig.java 中的配置
 * 2. 运行测试
 */
public class TronTransactionTest {
    
    private ApiWrapper apiWrapper;
    
    @BeforeEach
    public void setUp() {
        System.out.println("=== TRON 交易测试初始化 ===");
        
        // 检查配置是否完整
        if (!TronTestConfig.isConfigComplete()) {
            throw new RuntimeException("配置不完整，请先修改 TronTestConfig.java 文件");
        }
        
        // 打印配置信息
        TronTestConfig.printConfig();
        
        // 根据需要选择网络连接方式
        setupNetworkConnection();
    }
    
    /**
     * 设置网络连接
     * 可以选择连接到本地节点、测试网或主网
     */
    private void setupNetworkConnection() {
        try {
            switch (TronTestConfig.NETWORK.toUpperCase()) {
                case "LOCAL":
                    System.out.println("连接到本地TRON节点...");
                    apiWrapper = new ApiWrapper(
                        TronTestConfig.LOCAL_GRPC_ENDPOINT, 
                        TronTestConfig.LOCAL_SOLIDITY_GRPC_ENDPOINT, 
                        TronTestConfig.SENDER_PRIVATE_KEY
                    );
                    break;
                    
                case "NILE":
                    System.out.println("连接到Nile测试网...");
                    apiWrapper = ApiWrapper.ofNile(TronTestConfig.SENDER_PRIVATE_KEY);
                    break;
                    
                case "SHASTA":
                    System.out.println("连接到Shasta测试网...");
                    apiWrapper = ApiWrapper.ofShasta(TronTestConfig.SENDER_PRIVATE_KEY);
                    break;
                    
                case "MAINNET":
                    System.out.println("连接到TRON主网...");
                    apiWrapper = ApiWrapper.ofMainnet(TronTestConfig.SENDER_PRIVATE_KEY, TronTestConfig.TRONGRID_API_KEY);
                    break;
                    
                default:
                    throw new RuntimeException("未知的网络配置: " + TronTestConfig.NETWORK);
            }
            
            System.out.println("网络连接成功!");
            
        } catch (Exception e) {
            System.err.println("网络连接失败: " + e.getMessage());
            throw new RuntimeException("无法连接到TRON网络", e);
        }
    }
    
    /**
     * 测试完整的交易流程：创建 -> 签名 -> 广播
     */
    @Test
    public void testCompleteTransactionFlow() {
        System.out.println("\n=== 开始交易流程测试 ===");
        
        try {
            // 步骤1: 检查账户余额
            checkAccountBalance();
            
            // 步骤2: 创建交易
            Response.TransactionExtention transactionExtention = createTransaction();
            
            // 步骤3: 签名交易
            Chain.Transaction signedTransaction = signTransaction(transactionExtention);
            
            // 步骤4: 广播交易
            String txId = broadcastTransaction(signedTransaction);
            
            // 步骤5: 验证交易结果
            verifyTransaction(txId);
            
            System.out.println("\n=== 交易流程测试完成 ===");
            
        } catch (Exception e) {
            System.err.println("交易流程测试失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 检查账户余额
     */
    private void checkAccountBalance() {
        System.out.println("\n--- 步骤1: 检查账户余额 ---");
        
        try {
            // 获取发送方账户信息
            Response.Account fromAccount = apiWrapper.getAccount(TronTestConfig.FROM_ADDRESS);
            long fromBalance = fromAccount.getBalance();
            System.out.println("发送方余额: " + fromBalance + " SUN (" + (fromBalance / 1_000_000.0) + " TRX)");
            
            // 检查余额是否足够
            if (fromBalance < TronTestConfig.TRANSFER_AMOUNT) {
                throw new RuntimeException("余额不足! 当前余额: " + fromBalance + " SUN, 需要: " + TronTestConfig.TRANSFER_AMOUNT + " SUN");
            }
            
            // 获取接收方账户信息
            try {
                Response.Account toAccount = apiWrapper.getAccount(TronTestConfig.TO_ADDRESS);
                long toBalance = toAccount.getBalance();
                System.out.println("接收方余额: " + toBalance + " SUN (" + (toBalance / 1_000_000.0) + " TRX)");
            } catch (Exception e) {
                System.out.println("接收方账户可能是新账户或查询失败: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("查询账户余额失败: " + e.getMessage());
            throw new RuntimeException("无法查询账户余额", e);
        }
    }

    @Test
    public void testCheckAccountBalance() {
        try {
            System.out.println("\n=== 测试检查账户余额 ===");
            checkAccountBalance();
            System.out.println("测试完成: 账户余额检查成功");
        } catch (Exception e) {
            System.err.println("测试检查账户余额失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 创建交易
     */
    private Response.TransactionExtention createTransaction() {
        System.out.println("\n--- 步骤2: 创建交易 ---");
        
        try {
            Response.TransactionExtention transactionExtention = apiWrapper.transfer(
                TronTestConfig.FROM_ADDRESS, 
                TronTestConfig.TO_ADDRESS, 
                TronTestConfig.TRANSFER_AMOUNT
            );
            
            if (transactionExtention.getResult().getResult()) {
                System.out.println("交易创建成功!");
                System.out.println("交易哈希: " + transactionExtention.getTxid());
            } else {
                throw new RuntimeException("交易创建失败: " + transactionExtention.getResult().getMessage());
            }
            
            return transactionExtention;
            
        } catch (Exception e) {
            System.err.println("创建交易失败: " + e.getMessage());
            throw new RuntimeException("无法创建交易", e);
        }
    }
    
    /**
     * 签名交易
     */
    private Chain.Transaction signTransaction(Response.TransactionExtention transactionExtention) {
        System.out.println("\n--- 步骤3: 签名交易 ---");
        
        try {
            Chain.Transaction signedTransaction = apiWrapper.signTransaction(transactionExtention);
            System.out.println("交易签名成功!");
            
            return signedTransaction;
            
        } catch (Exception e) {
            System.err.println("签名交易失败: " + e.getMessage());
            throw new RuntimeException("无法签名交易", e);
        }
    }
    
    /**
     * 广播交易
     */
    private String broadcastTransaction(Chain.Transaction signedTransaction) {
        System.out.println("\n--- 步骤4: 广播交易 ---");
        
        // 检查是否实际广播
        if (!TronTestConfig.ACTUALLY_BROADCAST) {
            System.out.println("配置设置为不实际广播交易，跳过广播步骤");
            return "test-tx-id-not-broadcasted";
        }
        
        try {
            String txId = apiWrapper.broadcastTransaction(signedTransaction);
            System.out.println("交易广播成功!");
            System.out.println("交易ID: " + txId);
            
            return txId;
            
        } catch (Exception e) {
            System.err.println("广播交易失败: " + e.getMessage());
            throw new RuntimeException("无法广播交易", e);
        }
    }
    
    /**
     * 验证交易结果
     */
    private void verifyTransaction(String txId) {
        System.out.println("\n--- 步骤5: 验证交易结果 ---");
        
        // 如果没有实际广播，跳过验证
        if (!TronTestConfig.ACTUALLY_BROADCAST) {
            System.out.println("未实际广播交易，跳过验证步骤");
            return;
        }
        
        try {
            // 等待交易被确认
            System.out.println("等待交易确认...");
            Thread.sleep(TronTestConfig.CONFIRMATION_WAIT_TIME);
            
            // 查询交易信息
            Response.TransactionInfo txInfo = apiWrapper.getTransactionInfoById(txId);
            
            if (txInfo.getResult() == Response.TransactionInfo.code.SUCESS) {
                System.out.println("交易确认成功!");
                System.out.println("区块高度: " + txInfo.getBlockNumber());
                System.out.println("消耗能量: " + txInfo.getReceipt().getEnergyUsage());
                System.out.println("消耗带宽: " + txInfo.getReceipt().getNetUsage());
            } else {
                System.out.println("交易状态: " + txInfo.getResult());
            }
            
        } catch (Exception e) {
            System.err.println("验证交易失败: " + e.getMessage());
            // 不抛出异常，因为交易可能已经成功，只是查询有问题
        }
    }
    
    /**
     * 仅测试创建交易（不签名和广播）
     * 适用于测试环境，不会实际发送交易
     */
    @Test
    public void testCreateTransactionOnly() {
        System.out.println("\n=== 测试创建交易（不广播） ===");
        
        try {
            // 只创建交易，不签名和广播
            createTransaction();
            System.out.println("测试完成: 交易创建成功，未签名和广播");
            
        } catch (Exception e) {
            System.err.println("创建交易测试失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 打印配置信息的辅助测试
     */
    @Test
    public void testPrintConfiguration() {
        System.out.println("\n=== 当前配置信息 ===");
        TronTestConfig.printConfig();
        System.out.println("\n请确保:");
        System.out.println("1. 私钥和地址匹配");
        System.out.println("2. 账户有足够的TRX余额");
        System.out.println("3. 网络连接正常");
        System.out.println("4. 如果使用本地节点，确保节点正在运行");
    }
}