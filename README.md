# WantedGame
特定のプレイヤーを見つけるゲーム

## コマンド
### 処理コマンド
- /wg setLoc : 盤面の基準位置を設定(左下か右下)
- /wg setLoc x y z : 盤面の基準位置を設定(左下か右下)
- /wg setBoard <board_name>  : コンフィグを元にボードを設定
- /wg startGame <board_name> : ゲーム開始
- /wg endGame <board_name> : ゲーム終了
- /wg removeBoard <board_name> : ボードの削除
### コンフィグ設定
- /wg config setHeight <height> : 盤面の高さを設定
- /wg config setLength <length> : 盤面の長さ(横)を設定
- /wg config show : コンフィグを表示
- /wg config save : コンフィグを保存
- /wg config getData all/MCID : プレイヤー名のスキンデータを取得
- /wg config reload : コンフィグをリロード

## コンフィグ
### config.yml
| 変数名 | 説明 |
| ---- | ---- |
|  coordinate  |  基準座標データ x y z  |
|  height  |  盤面の高さ  |
| length | 盤面の長さ(横)|
### UUID_list.yml
UUID(ハイフン付き)\
    |-MCID : mcid\
    |-value : textureのvalue\
    |-signature : textureのsignature\
    |-skin : スキン(.png)をbase64エンコードしたもの
    ![2023-03-30_21 18 19](https://user-images.githubusercontent.com/115648249/228891001-f6c2e438-1e7e-4a52-ac69-a078610ba495.png)

    
