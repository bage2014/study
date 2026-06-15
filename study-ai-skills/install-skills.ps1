<#
.SYNOPSIS
AI Skills 安装脚本 - PowerShell 版本

.DESCRIPTION
将 AI Skills 安装到目标项目中，支持跨平台和多IDE配置

.PARAMETER Help
显示帮助信息

.PARAMETER List
列出可用的技能

.PARAMETER All
安装所有技能（默认）

.PARAMETER Skill
安装指定的技能（可多次使用）

.PARAMETER Force
强制覆盖已存在的技能

.PARAMETER Interactive
交互式安装模式

.PARAMETER Ide
指定目标IDE (trae/cursor/vscode/jetbrains/codeium)

.PARAMETER TargetDir
目标项目目录

.EXAMPLE
.\install-skills.ps1
安装所有技能到当前目录

.EXAMPLE
.\install-skills.ps1 -TargetDir "C:\projects\my-project"
安装所有技能到指定目录

.EXAMPLE
.\install-skills.ps1 -Skill "common-coding", "common-project-standard" -TargetDir "."
安装指定技能

.EXAMPLE
.\install-skills.ps1 -Interactive
交互式安装模式
#>

param (
    [switch]$Help,
    [switch]$List,
    [switch]$All = $true,
    [string[]]$Skill,
    [switch]$Force,
    [switch]$Interactive,
    [string]$Ide,
    [string]$TargetDir = "."
)

# 检测操作系统
function Detect-OS {
    if ($Env:OS -eq "Windows_NT") {
        return "windows"
    } elseif ($IsLinux) {
        return "linux"
    } elseif ($IsMacOS) {
        return "macos"
    } else {
        return "unknown"
    }
}

# 检测当前IDE
function Detect-IDE {
    $ide = "unknown"
    
    # 检测 Trae
    if ($Env:TRAE_HOME -or $Env:TRAE_PROJECT_DIR) {
        $ide = "trae"
    }
    # 检测 Cursor
    elseif ($Env:TERM_PROGRAM -eq "Cursor" -or $Env:CURSOR_HOME) {
        $ide = "cursor"
    }
    # 检测 VS Code
    elseif ($Env:TERM_PROGRAM -eq "vscode" -or $Env:VSCODE_CWD) {
        $ide = "vscode"
    }
    # 检测 JetBrains IDE
    elseif ($Env:TERMINAL_EMULATOR -eq "JetBrains-JediTerm") {
        $ide = "jetbrains"
    }
    # 检测 Codeium (CC)
    elseif ($Env:CODEIUM_DIR -or $Env:TERM_PROGRAM -eq "Codeium") {
        $ide = "codeium"
    }
    
    return $ide
}

# 获取IDE名称显示
function Get-IDE-Name {
    param($ide)
    switch ($ide) {
        "trae" { return "Trae" }
        "cursor" { return "Cursor" }
        "vscode" { return "VS Code" }
        "jetbrains" { return "JetBrains IDE (IntelliJ/CLion等)" }
        "codeium" { return "Codeium (CC)" }
        default { return "未知IDE" }
    }
}

# 获取系统名称显示
function Get-OS-Name {
    param($os)
    switch ($os) {
        "linux" { return "Linux" }
        "macos" { return "macOS" }
        "windows" { return "Windows" }
        default { return "未知系统" }
    }
}

# 显示欢迎信息
function Show-Welcome {
    $os = Detect-OS
    $ide = Detect-IDE
    
    Write-Host "=============================================" -ForegroundColor Cyan
    Write-Host "          AI Skills 安装脚本 (PowerShell)" -ForegroundColor Cyan
    Write-Host "=============================================" -ForegroundColor Cyan
    Write-Host "  系统: $(Get-OS-Name $os)" -ForegroundColor Green
    Write-Host "  IDE:  $(Get-IDE-Name $ide)" -ForegroundColor Green
    Write-Host "=============================================" -ForegroundColor Cyan
    Write-Host ""
}

# 显示帮助信息
function Show-Help {
    Write-Host "Usage: .\install-skills.ps1 [OPTIONS]"
    Write-Host ""
    Write-Host "将 AI Skills 安装到目标项目中"
    Write-Host ""
    Write-Host "Options:"
    Write-Host "  -Help             显示帮助信息"
    Write-Host "  -List             列出可用的技能"
    Write-Host "  -All              安装所有技能（默认）"
    Write-Host "  -Skill NAME       安装指定的技能（可多次使用）"
    Write-Host "  -Force            强制覆盖已存在的技能"
    Write-Host "  -Interactive      交互式安装模式"
    Write-Host "  -Ide IDE          指定目标IDE (trae/cursor/vscode/jetbrains/codeium)"
    Write-Host "  -TargetDir PATH   指定目标目录"
    Write-Host ""
    Write-Host "Examples:"
    Write-Host "  .\install-skills.ps1                          # 安装所有技能到当前目录"
    Write-Host "  .\install-skills.ps1 -TargetDir 'C:\project'  # 安装到指定目录"
    Write-Host "  .\install-skills.ps1 -Skill 'common-coding'   # 安装指定技能"
    Write-Host "  .\install-skills.ps1 -Interactive             # 交互式安装"
}

# 列出所有可用技能
function List-Skills {
    Write-Host "可用技能列表:"
    Write-Host ""
    
    # 个人定制技能
    Write-Host "=== 个人定制技能 (personal-*) ===" -ForegroundColor Yellow
    Get-ChildItem -Path $SKILLS_SOURCE_DIR -Directory -Filter "personal-*" | Sort-Object Name | ForEach-Object {
        $skillName = $_.Name
        $descFile = Join-Path $_.FullName "SKILL.md"
        if (Test-Path $descFile) {
            $desc = Select-String -Path $descFile -Pattern 'description: "([^"]+)"' | ForEach-Object { $_.Matches.Groups[1].Value }
        } else {
            $desc = $null
        }
        Write-Host "  $skillName"
        if ($desc) {
            Write-Host "      $desc"
        }
    }
    
    # 通用技能
    Write-Host ""
    Write-Host "=== 通用技能 (common-*) ===" -ForegroundColor Yellow
    Get-ChildItem -Path $SKILLS_SOURCE_DIR -Directory -Filter "common-*" | Sort-Object Name | ForEach-Object {
        $skillName = $_.Name
        $descFile = Join-Path $_.FullName "SKILL.md"
        if (Test-Path $descFile) {
            $desc = Select-String -Path $descFile -Pattern 'description: "([^"]+)"' | ForEach-Object { $_.Matches.Groups[1].Value }
        } else {
            $desc = $null
        }
        Write-Host "  $skillName"
        if ($desc) {
            Write-Host "      $desc"
        }
    }
    
    # 其他技能
    Write-Host ""
    Write-Host "=== 其他技能 ===" -ForegroundColor Yellow
    Get-ChildItem -Path $SKILLS_SOURCE_DIR -Directory | Where-Object {
        $_.Name -notlike "personal-*" -and 
        $_.Name -notlike "common-*" -and 
        $_.Name -ne "." -and 
        $_.Name -ne "skills"
    } | Sort-Object Name | ForEach-Object {
        $skillName = $_.Name
        $descFile = Join-Path $_.FullName "SKILL.md"
        if (Test-Path $descFile) {
            $desc = Select-String -Path $descFile -Pattern 'description: "([^"]+)"' | ForEach-Object { $_.Matches.Groups[1].Value }
        } else {
            $desc = $null
        }
        Write-Host "  $skillName"
        if ($desc) {
            Write-Host "      $desc"
        }
    }
}

# 获取技能列表
function Get-All-Skills {
    $skills = @()
    
    # 个人定制技能
    Get-ChildItem -Path $SKILLS_SOURCE_DIR -Directory -Filter "personal-*" | Sort-Object Name | ForEach-Object {
        $skills += $_.Name
    }
    
    # 通用技能
    Get-ChildItem -Path $SKILLS_SOURCE_DIR -Directory -Filter "common-*" | Sort-Object Name | ForEach-Object {
        $skills += $_.Name
    }
    
    # 其他技能
    Get-ChildItem -Path $SKILLS_SOURCE_DIR -Directory | Where-Object {
        $_.Name -notlike "personal-*" -and 
        $_.Name -notlike "common-*" -and 
        $_.Name -ne "." -and 
        $_.Name -ne "skills"
    } | Sort-Object Name | ForEach-Object {
        $skills += $_.Name
    }
    
    return $skills
}

# 交互式选择技能
function Interactive-Select-Skills {
    $allSkills = Get-All-Skills
    $selected = @()
    $i = 1
    
    Write-Host "请选择要安装的技能（输入数字，空格分隔，按回车确认，输入 'all' 选择全部）:"
    Write-Host ""
    
    foreach ($skill in $allSkills) {
        $descFile = Join-Path $SKILLS_SOURCE_DIR $skill "SKILL.md"
        if (Test-Path $descFile) {
            $desc = Select-String -Path $descFile -Pattern 'description: "([^"]+)"' | ForEach-Object { $_.Matches.Groups[1].Value }
        } else {
            $desc = $null
        }
        Write-Host "  $i. $skill"
        if ($desc) {
            Write-Host "     $desc"
        }
        $i++
    }
    
    Write-Host ""
    $inputVal = Read-Host "请输入选择"
    
    if ($inputVal -eq "all") {
        $selected = $allSkills
    } else {
        $nums = $inputVal -split " "
        foreach ($num in $nums) {
            $idx = [int]$num - 1
            if ($idx -ge 0 -and $idx -lt $allSkills.Length) {
                $selected += $allSkills[$idx]
            }
        }
    }
    
    return $selected
}

# 交互式选择IDE
function Interactive-Select-IDE {
    $ides = @("trae", "cursor", "vscode", "jetbrains", "codeium", "none")
    $names = @("Trae", "Cursor", "VS Code", "JetBrains IDE", "Codeium (CC)", "不配置IDE")
    $i = 1
    
    Write-Host "请选择目标IDE:"
    Write-Host ""
    
    for ($idx = 0; $idx -lt $ides.Length; $idx++) {
        Write-Host "  $i. $($names[$idx])"
        $i++
    }
    
    Write-Host ""
    $inputVal = Read-Host "请输入选择 (1-$($ides.Length))"
    
    $num = [int]$inputVal
    if ($num -ge 1 -and $num -le $ides.Length) {
        return $ides[$num - 1]
    } else {
        return "none"
    }
}

# 安装单个技能
function Install-Skill {
    param(
        [string]$skillName,
        [string]$targetDir,
        [bool]$force
    )
    
    $sourcePath = Join-Path $SKILLS_SOURCE_DIR $skillName
    $targetPath = Join-Path $targetDir $SKILLS_TARGET_DIR $skillName
    
    if (-not (Test-Path $sourcePath)) {
        Write-Host "错误: 技能 '$skillName' 不存在" -ForegroundColor Red
        exit 1
    }
    
    if (Test-Path $targetPath) {
        if (-not $force) {
            Write-Host "跳过 '$skillName' (已存在，使用 -Force 强制覆盖)" -ForegroundColor Yellow
            return
        } else {
            Remove-Item -Path $targetPath -Recurse -Force
        }
    }
    
    Write-Host "安装技能: $skillName"
    New-Item -ItemType Directory -Path $targetPath -Force | Out-Null
    Copy-Item -Path (Join-Path $sourcePath *) -Destination $targetPath -Recurse -Force
    Write-Host "  ✓ 已安装到 $targetPath" -ForegroundColor Green
}

# 配置IDE支持
function Configure-IDE {
    param(
        [string]$ide,
        [string]$targetDir
    )
    
    if ($ide -eq "none") {
        return
    }
    
    Write-Host ""
    Write-Host "=== 配置 $ide 支持 ===" -ForegroundColor Yellow
    
    # 创建IDE特定配置
    $ideConfigDir = Join-Path $targetDir ".trae" "ide"
    New-Item -ItemType Directory -Path $ideConfigDir -Force | Out-Null
    
    # 创建IDE配置文件
    $configContent = @"
# $ide IDE 配置
ide: $ide
enabled: true
settings:
  auto-load-skills: true
  skill-paths:
    - .trae/skills
"@
    $configFile = Join-Path $ideConfigDir "$ide.yml"
    $configContent | Out-File -FilePath $configFile -Encoding utf8
    Write-Host "  ✓ 已创建 $ide 配置" -ForegroundColor Green
    
    # 根据IDE类型添加特定配置
    switch ($ide) {
        "trae" {
            # Trae 特定配置
            $agentsContent = @"
# Trae 代理配置
agents:
  - name: skills-agent
    skills:
      - personal-backend-coding-standard
      - personal-frontend-coding-standard
      - common-project-standard
      - common-coding
"@
            $agentsFile = Join-Path $targetDir ".trae" "agents.yml"
            $agentsContent | Out-File -FilePath $agentsFile -Encoding utf8
            Write-Host "  ✓ 已创建 Trae 代理配置" -ForegroundColor Green
            break
        }
        "cursor", "vscode" {
            # VS Code/Cursor 配置
            $vscodeDir = Join-Path $targetDir ".vscode"
            New-Item -ItemType Directory -Path $vscodeDir -Force | Out-Null
            $settingsContent = @"
{
  "trae.skills.path": ".trae/skills",
  "trae.skills.autoLoad": true
}
"@
            $settingsFile = Join-Path $vscodeDir "settings.json"
            $settingsContent | Out-File -FilePath $settingsFile -Encoding utf8
            Write-Host "  ✓ 已创建 VS Code/Cursor 设置" -ForegroundColor Green
            break
        }
        "jetbrains" {
            # JetBrains IDE 配置
            $ideaDir = Join-Path $targetDir ".idea"
            New-Item -ItemType Directory -Path $ideaDir -Force | Out-Null
            $xmlContent = @"
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="TraeSkillsConfiguration">
    <option name="skillPath" string="true">.trae/skills</option>
    <option name="autoLoad" string="false">true</option>
  </component>
</project>
"@
            $xmlFile = Join-Path $ideaDir "trae-skills.xml"
            $xmlContent | Out-File -FilePath $xmlFile -Encoding utf8
            Write-Host "  ✓ 已创建 JetBrains IDE 配置" -ForegroundColor Green
            break
        }
        "codeium" {
            # Codeium (CC) 配置
            $codeiumDir = Join-Path $targetDir ".codeium"
            New-Item -ItemType Directory -Path $codeiumDir -Force | Out-Null
            $jsonContent = @"
{
  "skillsPath": ".trae/skills",
  "autoLoad": true
}
"@
            $jsonFile = Join-Path $codeiumDir "skills.json"
            $jsonContent | Out-File -FilePath $jsonFile -Encoding utf8
            Write-Host "  ✓ 已创建 Codeium 配置" -ForegroundColor Green
            break
        }
    }
}

# 主函数
function Main {
    # 检测环境
    $os = Detect-OS
    $detectedIde = Detect-IDE
    
    # 处理参数
    if ($Help) {
        Show-Help
        exit 0
    }
    
    if ($List) {
        List-Skills
        exit 0
    }
    
    # 显示欢迎信息
    Show-Welcome
    
    # 交互式模式
    if ($Interactive) {
        # 选择目标目录
        $inputDir = Read-Host "请输入目标项目目录 (默认: 当前目录)"
        if ($inputDir) {
            $script:TargetDir = $inputDir
        }
        
        # 选择技能
        $selectedSkills = Interactive-Select-Skills
        $script:Skill = $selectedSkills
        $script:All = $false
        
        # 选择IDE
        if (-not $Ide) {
            Write-Host ""
            Write-Host "检测到当前IDE: $(Get-IDE-Name $detectedIde)" -ForegroundColor Green
            $useDetected = Read-Host "是否使用检测到的IDE配置? (y/n)"
            if ($useDetected -eq "y" -or $useDetected -eq "Y") {
                $script:Ide = $detectedIde
            } else {
                $script:Ide = Interactive-Select-IDE
            }
        }
        
        # 确认安装
        Write-Host ""
        Write-Host "即将安装 $($Skill.Length) 个技能到 $TargetDir" -ForegroundColor Cyan
        Write-Host "目标IDE: $(Get-IDE-Name $Ide)" -ForegroundColor Cyan
        $confirm = Read-Host "确认安装? (y/n)"
        if ($confirm -ne "y" -and $confirm -ne "Y") {
            Write-Host "安装已取消" -ForegroundColor Yellow
            exit 0
        }
    }
    
    # 检查目标目录
    if (-not (Test-Path $TargetDir)) {
        Write-Host "错误: 目标目录 '$TargetDir' 不存在" -ForegroundColor Red
        exit 1
    }
    
    # 获取绝对路径
    $TargetDir = (Resolve-Path $TargetDir).Path
    
    # 创建目标技能目录
    $targetSkillsDir = Join-Path $TargetDir $SKILLS_TARGET_DIR
    New-Item -ItemType Directory -Path $targetSkillsDir -Force | Out-Null
    
    # 安装技能
    if ($All) {
        Write-Host "安装所有技能到 $TargetDir"
        Write-Host "=================================="
        
        # 安装个人定制技能
        Write-Host ""
        Write-Host "=== 安装个人定制技能 ===" -ForegroundColor Yellow
        Get-ChildItem -Path $SKILLS_SOURCE_DIR -Directory -Filter "personal-*" | Sort-Object Name | ForEach-Object {
            Install-Skill -skillName $_.Name -targetDir $TargetDir -force $Force
        }
        
        # 安装通用技能
        Write-Host ""
        Write-Host "=== 安装通用技能 ===" -ForegroundColor Yellow
        Get-ChildItem -Path $SKILLS_SOURCE_DIR -Directory -Filter "common-*" | Sort-Object Name | ForEach-Object {
            Install-Skill -skillName $_.Name -targetDir $TargetDir -force $Force
        }
        
        # 安装其他技能
        Write-Host ""
        Write-Host "=== 安装其他技能 ===" -ForegroundColor Yellow
        Get-ChildItem -Path $SKILLS_SOURCE_DIR -Directory | Where-Object {
            $_.Name -notlike "personal-*" -and 
            $_.Name -notlike "common-*" -and 
            $_.Name -ne "." -and 
            $_.Name -ne "skills"
        } | Sort-Object Name | ForEach-Object {
            Install-Skill -skillName $_.Name -targetDir $TargetDir -force $Force
        }
    } else {
        Write-Host "安装指定技能到 $TargetDir"
        Write-Host "=================================="
        foreach ($skill in $Skill) {
            Install-Skill -skillName $skill -targetDir $TargetDir -force $Force
        }
    }
    
    # 配置IDE
    if ($Ide) {
        Configure-IDE -ide $Ide -targetDir $TargetDir
    }
    
    Write-Host ""
    Write-Host "安装完成！" -ForegroundColor Green
    Write-Host ""
    Write-Host "技能已安装到: $targetSkillsDir" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "使用说明:" -ForegroundColor Cyan
    Write-Host "  1. 在项目根目录创建 AGENTS.md 文件，注册技能"
    Write-Host "  2. 创建 SKILLS.md 文件，列出可用技能"
    Write-Host "  3. 参考 common-project-standard 技能了解详细配置规范"
}

# 获取脚本绝对路径
$scriptPath = $MyInvocation.MyCommand.Definition
$SKILLS_SOURCE_DIR = Join-Path (Split-Path $scriptPath -Parent) ".trae" "skills"
$SKILLS_TARGET_DIR = ".trae/skills"

# 执行主函数
Main