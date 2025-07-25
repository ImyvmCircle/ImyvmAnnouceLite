# ImyvmAnnouceLite

一个轻量级 Minecraft Fabric 服务器端 Mod，用于定时广播 MOTD（消息）。

目前支持minecraft1.21。

## 功能说明

- 定时广播多条自定义消息
- 支持颜色代码与样式（使用 `&` 前缀）
- 动态管理广播内容（添加、删除、查看、重载）
- 仅服务器管理员可修改广播内容

## 指令列表

| 指令                      | 权限要求 | 说明                           |
|---------------------------|-----------|--------------------------------|
| `/imyvm-motd list`        | 任意用户  | 查看所有 MOTD 消息              |
| `/imyvm-motd add <msg>`   | OP        | 添加一条新的广播消息           |
| `/imyvm-motd remove <id>` | OP        | 移除指定索引的广播消息         |
| `/imyvm-motd reload`      | OP        | 重新加载配置文件中的 MOTD 列表 |

## 配置项（config/imyvm-annouce.conf）

```hocon
core {
  imyvm-annouce {
    interval_seconds = 30  # 每条消息之间的间隔（单位：秒）
    motd = [
      "&a欢迎来到服务器！",
      "&b记得遵守规则，保持友善交流～"
    ]
  }
}
```
## 构建与使用
### 开发环境要求
- Java 21
- Fabric Loader & API

### 构建方式
```bash
./gradlew build
```
构建产物位于 build/libs/，将生成的 .jar 文件放入 mods/ 目录中。

## 许可协议

本项目采用 [GNU 通用公共许可证 第3版 (GPLv3)](https://www.gnu.org/licenses/gpl-3.0.html) 发布。

这意味着你可以自由地使用、修改和再发布本项目，前提是：
- 所有派生作品必须在同样的 GPLv3 协议下发布；
- 在发布时必须附带原始的许可证文本；
- 必须保留原始作者的版权声明。

详见项目中的 [LICENSE](./LICENSE) 文件。
