# Material 3 Expressive リファレンス

## 概要

Material 3 Expressiveは、Material Design 3の最新進化形。Android 16のビジュアルスタイルとシステムUIを補完するよう設計されている。

## 依存関係

```toml
# gradle/libs.versions.toml
[versions]
composeBom = "2025.05.00"
composeMaterial3 = "1.5.0-alpha"

[libraries]
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
```

## OptInアノテーション

Expressiveコンポーネントは実験的APIのため、必ず付与:

```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyScreen() { ... }
```

---

## テーマ設定

### MotionScheme

アプリ全体のモーション設定を制御:

```kotlin
MaterialTheme(
    colorScheme = colorScheme,
    typography = typography,
    shapes = shapes,
    motionScheme = MotionScheme.expressive() // または standard()
) {
    // Content
}
```

### 5つのキーカラー

| カラー | 用途 |
|--------|------|
| Primary | 主要なUI要素 |
| Secondary | 二次的な要素 |
| Tertiary | アクセントカラー |
| Error | エラー表示 |
| Surface | 背景やカード |

### Shapes（形状）

```kotlin
val shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)
```

### Typography（タイポグラフィ）

```kotlin
val typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp
    ),
    // display, headline, title, body, label の各サイズ
)
```

---

## コンポーネント詳細

### LoadingIndicator

短い待機時間（5秒未満）に使用。形状がモーフィングするアニメーション。

```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyLoadingScreen() {
    // 基本
    LoadingIndicator()

    // コンテナ付き
    ContainedLoadingIndicator()
}
```

### DockedToolbar

`BottomAppBar`の代替。より短く柔軟なデザイン。

```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyDockedToolbar() {
    DockedToolbar {
        IconButton(onClick = { }) {
            Icon(Icons.Default.Home, contentDescription = "Home")
        }
        IconButton(onClick = { }) {
            Icon(Icons.Default.Search, contentDescription = "Search")
        }
    }
}
```

### FloatingToolbar

水平・垂直両方向に対応。FABと組み合わせ可能。

```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyFloatingToolbar() {
    FloatingToolbar(
        expanded = expanded,
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        // ツールバーアイテム
    }
}
```

### FlexibleBottomAppBar

スクロール動作に応じてレイアウトと表示を動的に調整。

```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyFlexibleBottomAppBar() {
    FlexibleBottomAppBar(
        scrollBehavior = scrollBehavior
    ) {
        // コンテンツ
    }
}
```

---

## 参考リンク

- [Material 3 公式ドキュメント](https://developer.android.com/develop/ui/compose/designsystems/material3)
- [Material 3 Expressive ブログ](https://m3.material.io/blog/building-with-m3-expressive)
- [サンプルカタログ](https://github.com/meticha/material-3-expressive-catalog)