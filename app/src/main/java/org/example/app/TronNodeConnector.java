package org.example.app;

import org.tron.trident.core.ApiWrapper;

/**
 * Tron Node Connector - 用于连接本地或远程 TRON 节点的工具类
 */
public class TronNodeConnector {
    
    /**
     * 连接到本地 TRON 节点
     * @param privateKey 私钥（可选，如果只是查询可以为空）
     * @return ApiWrapper 实例
     */
    public static ApiWrapper connectToLocalNode(String privateKey) {
        // 本地节点默认端口
        String grpcEndpoint = "127.0.0.1:50051";           // gRPC 端点
        String solidityGrpcEndpoint = "127.0.0.1:50061";   // Solidity gRPC 端点
        
        System.out.println("正在连接到本地 TRON 节点...");
        System.out.println("gRPC 端点: " + grpcEndpoint);
        System.out.println("Solidity gRPC 端点: " + solidityGrpcEndpoint);
        
        return new ApiWrapper(grpcEndpoint, solidityGrpcEndpoint, privateKey);
    }
    
    /**
     * 连接到 Nile 测试网
     * @param privateKey 私钥
     * @return ApiWrapper 实例
     */
    public static ApiWrapper connectToNileTestnet(String privateKey) {
        System.out.println("正在连接到 Nile 测试网...");
        return ApiWrapper.ofNile(privateKey);
    }
    
    /**
     * 连接到 Shasta 测试网
     * @param privateKey 私钥
     * @return ApiWrapper 实例
     */
    public static ApiWrapper connectToShastaTestnet(String privateKey) {
        System.out.println("正在连接到 Shasta 测试网...");
        return ApiWrapper.ofShasta(privateKey);
    }
    
    /**
     * 连接到主网
     * @param privateKey 私钥
     * @param apiKey TronGrid API 密钥
     * @return ApiWrapper 实例
     */
    public static ApiWrapper connectToMainnet(String privateKey, String apiKey) {
        System.out.println("正在连接到 TRON 主网...");
        return ApiWrapper.ofMainnet(privateKey, apiKey);
    }
    
    /**
     * 测试连接是否成功
     * @param client ApiWrapper 实例
     */
    public static void testConnection(ApiWrapper client) {
        try {
            // 这里可以添加一些简单的查询来测试连接
            System.out.println("连接测试：正在查询网络信息...");
            // 注意：实际的 API 调用需要根据 Trident 的具体版本来确定
            System.out.println("连接成功！");
        } catch (Exception e) {
            System.err.println("连接测试失败: " + e.getMessage());
        }
    }
}