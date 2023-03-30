# WantedGame
特定のプレイヤーを見つけるゲーム

- 時間制限あり
- 正解する毎に5秒プラス
- 間違えると1秒マイナス
- 初期時間は20秒
- 毎回インベントリの一つ目にプレイヤーの名前が書かれた地図を付与

## 遊び方
- /wg setLoc で基準座標を設定
- /wg setBoard <board_name> でボードを設定
- /wg startGame <board_name> でゲームを開始

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
|  coordinate   |  基準座標データ x y z  |
|   coordinate.x|x座標|
|   coordinate.y|y座標|
|   coordinate.z|z座標|
|  height  |  盤面の高さ  |
| length | 盤面の長さ(横)|
### UUID_list.yml
- UUID(ハイフン付き) 
    - MCID : mcid 
    - value : textureのvalue
    - signature : textureのsignature
    - skin : スキン(.png)をbase64エンコードしたもの
    ![2023-03-30_21 18 19](https://user-images.githubusercontent.com/115648249/228891001-f6c2e438-1e7e-4a52-ac69-a078610ba495.png)

    
