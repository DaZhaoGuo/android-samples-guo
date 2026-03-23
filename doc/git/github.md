## gh

GitHub CLI (gh) 是 GitHub 官方推出的开源命令行工具，旨在将 GitHub 的功能（如 PR、Issue、Actions）直接引入本地终端。它允许开发者在不离开命令行的情况下进行代码仓库管理、查看审阅、自动化工作流等操作，从而避免切换浏览器上下文，显著提高工作效率。

gh CLI 的主要用途：
* 高效管理仓库：通过命令即可快速克隆 (gh repo clone)、创建 (gh repo create) 或分叉 (gh repo fork) 仓库。
* PR 和 Issue 处理：无需切换到网页，即可在终端创建、查看、合并拉取请求 (PR) 以及管理 Issue。
* 自动化 GitHub Actions：使用 gh workflow 命令直接在命令行运行或查看工作流情况。
* 管理 Gist 和 Codespaces：支持在本地终端管理 Gist 和连接到云端开发环境 (Codespaces)。
* 扩展性：支持自定义扩展，进一步增强工具的功能。

安装后，通过运行 gh auth login 即可登录并开始使用
