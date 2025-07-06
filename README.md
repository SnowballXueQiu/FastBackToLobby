# FastBackToLobby

一个用于快速返回大厅服务器的 Bukkit/Spigot/Paper 插件，支持 BungeeCord 和 Velocity 代理服务器。

A Bukkit/Spigot/Paper plugin for quickly returning to lobby server, supports BungeeCord and Velocity proxy servers.

## 功能特性 / Features

- 🚀 快速传送到大厅服务器 / Quick teleport to lobby server
- 🎮 多个命令别名支持 / Multiple command aliases support
- 🌍 多语言支持 / Multi-language support
- ⏰ 冷却时间系统 / Cooldown system
- 🔊 音效支持 / Sound effects support
- ⚙️ 高度可配置 / Highly configurable
- 🔄 热重载配置 / Hot reload configuration

## 支持的服务端 / Supported Servers

- Bukkit 1.13+
- Spigot 1.13+
- Paper 1.13+
- 其他基于 Bukkit API 的服务端 / Other Bukkit API based servers

## 安装 / Installation

1. 下载插件 jar 文件 / Download the plugin jar file
2. 将文件放入服务器的 `plugins` 文件夹 / Place the file in the server's `plugins` folder
3. 重启服务器 / Restart the server
4. 编辑配置文件 `plugins/FastBackToLobby/config.yml` / Edit the config file `plugins/FastBackToLobby/config.yml`
5. 使用 `/fastbacktolobby reload` 重载配置 / Use `/fastbacktolobby reload` to reload config

## 配置 / Configuration

### config.yml

```yaml
# 大厅服务器名称 (BungeeCord/Velocity 服务器名称)
lobby-server: "lobby"

# 语言设置 (zh_CN, en_US 等)
language: "en_US"

# 启用调试模式
debug: false

# 冷却时间（秒）（0为禁用）
cooldown: 3

# 传送时启用音效
enable-sound: true

# 播放的音效
sound: "ENTITY_ENDERMAN_TELEPORT"

# 将玩家发送到大厅的命令
commands:
  - "quit"
  - "l"
  - "lobby"
  - "leave"
  - "hub"
```

## 命令 / Commands

| 命令 / Command | 描述 / Description | 权限 / Permission |
|---|---|---|
| `/quit` | 返回大厅 / Return to lobby | `fastbacktolobby.use` |
| `/l` | 返回大厅 / Return to lobby | `fastbacktolobby.use` |
| `/lobby` | 返回大厅 / Return to lobby | `fastbacktolobby.use` |
| `/leave` | 返回大厅 / Return to lobby | `fastbacktolobby.use` |
| `/hub` | 返回大厅 / Return to lobby | `fastbacktolobby.use` |
| `/fastbacktolobby reload` | 重载配置 / Reload config | `fastbacktolobby.admin.reload` |
| `/fastbacktolobby permissions` | 查看权限信息 / View permissions info | 无 / None |

## 权限 / Permissions

| 权限 / Permission | 描述 / Description | 默认 / Default |
|---|---|---|
| `fastbacktolobby.*` | 授予所有权限 / Grants all permissions | `false` |
| `fastbacktolobby.use` | 允许使用大厅命令 / Allow using lobby commands | `true` |
| `fastbacktolobby.admin` | 授予所有管理员权限 / Grants all admin permissions | `op` |
| `fastbacktolobby.admin.reload` | 允许重载配置 / Allow reloading configuration | `op` |
| `fastbacktolobby.bypass.cooldown` | 允许绕过冷却时间 / Allow bypassing cooldown | `op` |

## 多语言支持 / Multi-language Support

插件支持多语言，语言文件位于 `plugins/FastBackToLobby/lang/` 目录下：

The plugin supports multiple languages, language files are located in `plugins/FastBackToLobby/lang/` directory:

- `zh_CN.yml` - 简体中文 / Simplified Chinese
- `en_US.yml` - 英语 / English

你可以创建自己的语言文件，文件名格式为 `语言代码.yml`。

You can create your own language files with the format `language_code.yml`.

## BungeeCord/Velocity 配置 / BungeeCord/Velocity Configuration

### BungeeCord

确保在 `spigot.yml` 中启用 BungeeCord 支持：

Make sure BungeeCord support is enabled in `spigot.yml`:

```yaml
settings:
  bungeecord: true
```

### Velocity

确保在 `paper.yml` 或 `velocity-support.yml` 中启用 Velocity 支持。

Make sure Velocity support is enabled in `paper.yml` or `velocity-support.yml`.

## 编译 / Building

```bash
mvn clean package
```

编译后的 jar 文件将位于 `target/` 目录下。

The compiled jar file will be located in the `target/` directory.

## 许可证 / License

MIT License

## 支持 / Support

如果你遇到任何问题或有功能建议，请在 GitHub 上创建 issue。

If you encounter any issues or have feature suggestions, please create an issue on GitHub.

## 更新日志 / Changelog

### v1.0.0
- 初始版本 / Initial release
- 基本的大厅传送功能 / Basic lobby teleport functionality
- 多语言支持 / Multi-language support
- 冷却时间系统 / Cooldown system
- 音效支持 / Sound effects support
