package com.project.armbreaker.modules.screen.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.armbreaker.R

//class TrashCodes {
//}
//Image(
//            modifier = Modifier,
//            painter = rememberDrawablePainter(
//                drawable = getDrawable(
//                    LocalContext.current,
//                    R.drawable.game_background
//                )
//            ),
//            contentDescription = "People Cheer Animation",
//            contentScale = ContentScale.Crop,
//        )

//TRY AGAIN
//        GlideImage(
//            model = R.drawable.game_background,
//            contentDescription = "Game Background",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )

//Button(onClick = {
//                    gameViewModel.restartGame()
//                }, modifier = Modifier
//                    .padding(2.dp),
//                    enabled = gameViewModel.allowRestart,
//                    shape = RectangleShape
//                ){
//                    Text(
//                        text = "Restart",
//                        fontSize = 20.sp,
//                    )
//                }
//                Button(onClick = {
//                    navController.navigate("home")
//                }, modifier = Modifier
//                    .padding(2.dp),
//                    shape = RectangleShape
//                ){
//                    Text(
//                        text = "Back",
//                        fontSize = 20.sp
//                    )
//                }


//Exit button
//ButtonLayout(
//image = R.drawable.red_button,
//icon = Exit,
//size = 35.dp,
//modifier = Modifier
//.weight(1f)
//.aspectRatio(0.7f),
//){
//    navController.popBackStack()
//}
//Spacer(modifier = Modifier.width(8.dp)) // Small spacing between buttons
////Play button
//ButtonLayout(
//image = R.drawable.green_button,
//icon = R.drawable.play_icon,
//size = 35.dp,
//modifier = Modifier
//.weight(1f)
//.aspectRatio(0.7f)
//){
//    gameViewModel.continueGame()
//}
//Spacer(modifier = Modifier.width(8.dp)) // Small spacing between buttons
////Restart button
//ButtonLayout(
//image = R.drawable.yellow_button,
//icon = Restart,
//size = 35.dp,
//modifier = Modifier
//.weight(1f)
//.aspectRatio(0.7f)
//){
//    gameViewModel.restartGame()
//}

//Row(
//modifier = Modifier
//.weight(1f)
//.fillMaxWidth()
//.padding(start = 10.dp, top = 16.dp),
//verticalAlignment = Alignment.CenterVertically,
//horizontalArrangement = Arrangement.Start
//) {
//    com.project.armbreaker.modules.game.ui.ButtonLayout(
//        image = R.drawable.purple_button,
//        icon = R.drawable.pause_icon,
//        size = 25.dp,
//        modifier = Modifier
//            .width(70.dp)
//            .aspectRatio(1f),
//        offset = (-5).dp
//    ) {
//        gameViewModel.pauseGame()
//    }
//    // Add a spacer to push everything else to the right
//    Spacer(modifier = Modifier.weight(1f))
//}