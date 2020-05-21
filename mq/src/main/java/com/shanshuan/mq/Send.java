package com.shanshuan.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 * 　　┃　　　┃神兽保佑
 * 　　┃　　　┃代码无BUG！
 * 　　┃　　　┗━━━┓
 * 　　┃　　　　　　　┣┓
 * 　　┃　　　　　　　┏┛
 * 　　┗┓┓┏━┳┓┏┛
 * 　　　┃┫┫　┃┫┫
 * 　　　┗┻┛　┗┻┛
 *
 *                      _ooOoo_
 * 	                  o8888888o
 * 	                  88" . "88
 * 	                  (| -_- |)
 * 	                  O\  =  /O
 * 	               ____/`---'\____
 * 	             .'  \\|     |//  `.
 * 	            /  \\|||  :  |||//  \
 * 	           /  _||||| -:- |||||-  \
 * 	           |   | \\\  - / |   |
 * 	           | \_|  ''\-/''  |   |
 * 	           \  .-\__  `-`  ___/-. /
 * 	         ___`. .'  /-.-\  `. . __
 * 	      ."" '<  `.___\_<|>_/___.'  >'"".
 * 	     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * 	     \  \ `-.   \_ __\ /__ _/   .-` /  /
 * 	======`-.____`-.___\_____/___.-`____.-'======
 * 	                   `=-='
 //          佛曰:
 //                  写字楼里写字间，写字间里程序员；
 //                  程序人员写程序，又拿程序换酒钱。
 //                  酒醒只在网上坐，酒醉还来网下眠；
 //                  酒醉酒醒日复日，网上网下年复年。
 //                  但愿老死电脑间，不愿鞠躬老板前；
 //                  奔驰宝马贵者趣，公交自行程序员。
 //                  别人笑我忒疯癫，我笑自己命太贱；
 //                  不见满街漂亮妹，哪个归得程序员？
 *
 *
 *┌───┐   ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│   │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│  ┌┐    ┌┐    ┌┐
 *└───┘   └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘  └┘    └┘    └┘
 *┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐ ┌───┬───┬───┐ ┌───┬───┬───┬───┐
 *│~ `│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp │ │Ins│Hom│PUp│ │N L│ / │ * │ - │
 * ├───┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤ ├───┼───┼───┤ ├───┼───┼───┼───┤
 *│ Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ │ │Del│End│PDn│ │ 7 │ 8 │ 9 │   │
 *├─────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤ └───┴───┴───┘ ├───┼───┼───┤ + │
 *│ Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │               │ 4 │ 5 │ 6 │   │
 *├──────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤     ┌───┐     ├───┼───┼───┼───┤
 *│ Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │     │ ↑ │     │ 1 │ 2 │ 3 │   │
 *├─────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤ ┌───┼───┼───┐ ├───┴───┼───┤ E││
 *│ Ctrl│    │Alt │         Space         │ Alt│    │    │Ctrl│ │ ← │ ↓ │ → │ │   0   │ . │←─┘│
 *└─────┴────┴────┴───────────────────────┴────┴────┴────┴────┘ └───┴───┴───┘ └───────┴───┴───┘
 *
 *
 *                    .::::.
 *                  .::::::::.
 *                 :::::::::::  I && YOU
 *             ..:::::::::::'
 *           '::::::::::::'
 *             .::::::::::
 *        '::::::::::::::..
 *             ..::::::::::::.
 *           ``::::::::::::::::
 *            ::::``:::::::::'        .:::.
 *           ::::'   ':::::'       .::::::::.
 *         .::::'      ::::     .:::::::'::::.
 *        .:::'       :::::  .:::::::::' ':::::.
 *       .::'        :::::.:::::::::'      ':::::.
 *      .::'         ::::::::::::::'         ``::::.
 *  ...:::           ::::::::::::'              ``::.
 * ````':.          ':::::::::'                  ::::..
 *                    '.:::::'                    ':'````..
 * 	                   
 *
 * 
 *                      d*##$.
 * zP"""""$e.           $"    $o
 *4$       '$          $"      $
 *'$        '$        J$       $F
 * 'b        $k       $>       $
 *  $k        $r     J$       d$
 *  '$         $     $"       $~
 *   '$        "$   '$E       $
 *    $         $L   $"      $F ...
 *     $.       4B   $      $$$*"""*b
 *     '$        $.  $$     $$      $F
 *      "$       R$  $F     $"      $
 *       $k      ?$ u*     dF      .$
 *       ^$.      $$"     z$      u$$$$e
 *        #$b             $E.dW@e$"    ?$
 *         #$           .o$$# d$$$$c    ?F
 *          $      .d$$#" . zo$>   #$r .uF
 *          $L .u$*"      $&$$$k   .$$d$$F
 *           $$"            ""^"$$$P"$P9$
 *          JP              .o$$$$u:$P $$
 *          $          ..ue$"      ""  $"
 *         d$          $F              $
 *         $$     ....udE             4B
 *          #$    """"` $r            @$
 *           ^$L        '$            $F
 *             RN        4N           $
 *              *$b                  d$
 *               $$k                 $F
 *               $$b                $F
 *                 $""               $F
 *                 '$                $
 *                  $L               $
 *                  '$               $
 *                   $               $
 *
 *
 * 
 *                .-~~~~~~~~~-._       _.-~~~~~~~~~-.
 *            __.'              ~.   .~              `.__
 *          .'//   程序员自我修养   \./   如何快速写bug    \\`.
 *        .'//                     |                     \\`.
 *      .'// .-~"""""""~~~~-._     |     _,-~~~~"""""""~-. \\`.
 *    .'//.-"                 `-.  |  .-'                 "-.\\`.
 *  .'//______.============-..   \ | /   ..-============.______\\`.
 *.'______________________________\|/______________________________`.

 *
 *
 *                         __
 *                     ,-~¨^  ^¨-,           _,
 *                    /          / ;^-._...,¨/
 *                   /          / /         /
 *                  /          / /         /
 *                 /          / /         /
 *                /,.-:''-,_ / /         /
 *                _,.-:--._ ^ ^:-._ __../
 *              /^         / /¨:.._¨__.;
 *             /          / /      ^  /
 *            /          / /         /
 *           /          / /         /
 *          /_,.--:^-._/ /         /
 *         ^            ^¨¨-.___.:^  (R) - G33K
 *
 *                      _.---..._
 *                    ./^         ^-._
 *                  ./^C===.         ^\.   /\
 *                 .|'     \\        _ ^|.^.|
 *            ___.--'_     ( )  .      ./ /||
 *           /.---^T\      ,     |     / /|||
 *          C'   ._`|  ._ /  __,-/    / /-,||
 *               \ \/    ;  /O  / _    |) )|,
 *                i \./^O\./_,-^/^    ,;-^,'
 *                 \ |`--/ ..-^^      |_-^
 *                  `|  \^-           /|:
 *                   i.  .--         / '|.
 *                    i   =='       /'  |\._
 *                  _./`._        //    |.  ^-ooo.._
 *           _.oo../'  |  ^-.__./X/   . `|    |#######b
 *          d####     |'      ^^^^   /   |    _\#######
 *          #####b ^^^^^^^^--. ...--^--^^^^^^^_.d######
 *          ######b._         Y            _.d#########
 *          ##########b._     |        _.d#############
 *
 *
 *
 *      _
 *                            \"-._ _.--"~~"--._
 *                             \   "            ^.    ___
 *                             /                  \.-~_.-~
 *                      .-----'     /\/"\ /~-._      /
 *                     /  __      _/\-.__\L_.-/\     "-.
 *                    /.-"  \    ( ` \_o>"<o_/  \  .--._\
 *                   /'      \    \:     "     :/_/     "`
 *                           /  /\ "\    ~    /~"
 *                           \ I  \/]"-._ _.-"[
 *                        ___ \|___/ ./    l   \___   ___
 *                   .--v~   "v` ( `-.__   __.-' ) ~v"   ~v--.
 *                .-{   |     :   \_    "~"    _/   :     |   }-.
 *               /   \  |           ~-.,___,.-~           |  /   \
 *              ]     \ |                                 | /     [
 *              /\     \|     :                     :     |/     /\
 *             /  ^._  _K.___,^                     ^.___,K_  _.^  \
 *            /   /  "~/  "\                           /"  \~"  \   \
 *           /   /    /     \ _          :          _ /     \    \   \
 *         .^--./    /       Y___________l___________Y       \    \.--^.
 *         [    \   /        |        [/    ]        |        \   /    ]
 *         |     "v"         l________[____/]________j  -Row   }r"     /
 *         }------t          /                       \       /`-.     /
 *         |      |         Y                         Y     /    "-._/
 *         }-----v'         |         :               |     7-.     /
 *         |   |_|          |         l               |    / . "-._/
 *         l  .[_]          :          \              :  r[]/_.  /
 *          \_____]                     "--.             "-.____/
 *
 *
 *
 *                                 MMMMM
 *                                   MMMMMM
 *                                     MMMMMMM
 *                                      MMMMMMMM     .
 *                                       MMMMMMMMM
 *                                       HMMMMMMMMMM
 *                                        MMMMMMMMMMMM  M
 *                                        MMMMMMMMMMMMM  M
 *                                         MMMMMMMMMMMMM  M
 *                                         MMMMMMMMMMMMM:
 *                                         oMMMMMMMMMMMMMM
 *               .MMMMMMMMMMMMMMo           MMMMMMMMMMMMMMM M
 *         MMMMMMMMMMMMMMMMMMMMMMMMMMM      MMMMMMMMMMMMMMMM
 *           MMMMMMMMMMMMMMMMMMMMMMMMMMMM.  oMMMMMMMMMMMMMMM.M
 *             MMMMMMMMMMMMMMMMMMMMMMMMMMMM  MMMMMMMMMMMMMMMM
 *               MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 *                 oMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 *                   MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 *                     MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM:                     H
 *                      MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM                  .         MMM
 *                       MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM              M       MMMMMM
 *                        .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM          M   MMMMMMMMMM
 *                 MM.      MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM       M MMMMMMMMMMMM
 *                     MM    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM    .MMMMMMMMMMMMMM
 *                       MM  MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 *                         MM MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 *                .MMMMMMMMM MMMMMMMMMMMMMMMMMMMMMMMM.MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 *                   HMMMMMMMMMMMMMMMMMMMMM.MMMMMMMMM.MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 *                      MMMMMMMMMMMMMMM MMM.oMMMMMMM..MMMMMMMMM:MMMMMMMMMMMMMMMMMMMMMMM
 *                        MMMMMMMMMMMMMM MM..MMMMMMM...MMMMMMM. MMMMMMMMMMMMMMMMMMMMM
 *                          MMMMMMMMMMMMMMM ..MMMMMM...MMMMMM ..MMMMMMMMMMMMMMMMMMM
 *                           MMMMMMM:M.MMM.M.. MMMMM M..MMMMM...MMMMMMMMMMMMMMMMMM  MMM
 *                             MMMM. .M..MM.M...MMMMMM..MMMMM.. MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM .
 *                              MMMM..M....M.....:MMM .MMMMMM..MMMMMMM...MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 *                               MMM.M.. ...M......MM.MMMMM.......MHM.M  .MMMMMMMMMMMMMMMMMMMMMMMMM
 *                          MMMMMMMM..MM. . MMM.....MMMMMM.M.....M ..MM..M MMMMMMMMMMMMMMMMMMM
 *                             .MMMMMHMM. ..MMMM. MMM............o..... . .MMMMMMMMMMMMMMM
 *                                MMM. M... .........................M..:.MMMMMMMMMMMM
 *                                  oMMM............ .................M.M.MMMMMMMMM
 *                                     .....MM........................ . MMMMMM
 *                                    M.....M.....................o.MM.MMMMMMMM.
 *                                     M........................M.. ...MMMMMMMMMMMMMo
 *                                       :....MMM..............MMM..oMMMMMMM
 *                                        M...MMM.............MMMMMMM
 *                                           .............:MMMMMMMM
 *                                           M..... MMM.....M
 *                                           M M.............
 *                                           ................M
 *                                        ooM.................MM  MoMMMMMoooM
 *                                   MMoooM......................MoooooooH..oMM
 *                               MHooooMoM.....................MMooooooM........M
 *                             oooooooMoooM......... o........MoooooooM............
 *                             Mooooooooooo.......M.........Moooooooo:..............M
 *                            MooMoooooooooM...M........:Mooooooooooo:..............M
 *                           M..oooooooooooo .........Mooooooooooooooo..............M
 *                          M...Mooo:oooooooo.M....ooooooooooooooooooo..M...........M
 *                           ...oooooMoooooooM..Mooooooooooooo:oooooooM.M...........M.
 *                          M...ooooooMoo:ooooMoooooooooooooHoooooooooH:M. ...........:
 *                          M..MoooooooMoooooooooooooooooo:ooooooMooooMoM..............M
 *                          M..ooooooooooMooooooooooooooHoooooooMooHooooM...............M
 *                          ...ooooooooooooooooooo:MooooooooooooooMoMoooM................
 *                         M...oooooooooooooooooooooooooooooooooooooMooMM................M
 *                         ...MooooooooooooooooooooooooooooooooooooooooMo ................
 *                         ...MooooooooooooooooooooooooooooooooooooooooM M................M
 *                        M...ooooooooooooooooooooooooooooooooooooooooM   ................M
 *                        ...MoooooooooooooooooooooooooooooooooooooooMM   .:...............
 *                        .....MooooooooooooooooooooooooooooooooooooMoo       .............M
 *                        M...... ooooooooooooooooooooooooooooooooooooM       M..............M
 *                        M........MooooMMM MM MM  MMMMMMMMMooooooooM         M...............M
 *                        .........HM     M:  MM :MMMMMM          M           M...............
 *                       M..........M     M   MoM M                           M................M
 *                       M.........:M  MoH  M M M MooooHoooMM.   M             M...............M
 *                       M..........Moooo MMooM    oooooMooooooooM              M..............H
 *                       M.........MooooM  Mooo  : ooooooMooooMoooM              M........ . .o.M
 *                       H..  .....ooooo   oooo  M MooooooooooooooM               M... MMMMMMMMMMM
 *                       MMMMMMMMMMooooM M oooo  .  ooooooMooooooooM              .MMMMMMMMMMMMMMM
 *                       MMMMMMMMMMooooH : ooooH    oooooooooooooooo               MMMMMMMMMMMMMMM
 *                       MMMMMMMMMMoooo    ooooM    Moooooooooooooooo              .MMMMMMMMMMMMMMM
 *                       MMMMMMMMMMoooo    ooooM    MooooooooooooooooM              MMMMMMMMMMMMMMM
 *                       MMMMMMMMMMoooM    ooooM     ooooooooooooooooo               MMMMMMMMMMM:M
 *                       MMMMMMMMMMoooM   MooooM     oooooooooooMoooooo               MH...........
 *                        . ......Mooo.   MooooM     oooooooooooooooooo              M............M
 *                       M.M......oooo    MooooM     Moooooooooooooooooo:           .........M.....
 *                       M.M.....Moooo    MooooM      ooooooooooooooooooM            .M............
 *                       .......MooooH    MooooM      oooooooooMoooooooooo          M..o...M..o....M
 *                       .o....HMooooM    MooooH      MooooooooMooooooooooM          .:M...M.......M
 *                      M..M.....MoooM    :oooo:    .MooooooooHooMoooooooooM         M M... ..oM.M
 *                       M...M.:.Mooo. MMMMooooo   oooooooooooMoooooooooooooM          ....M. M
 *                        M:M..o.Moooooooooooooo MooooooooooooooMooooooooooooM          .Mo
 *                               MooooooooooooooMooooooooooooMoMoooooooooooooo
 *                               Mooooooooooooooo:ooooooooooooooooooooooooooooo
 *                               ooooooooooooooooMooooooooooMoooooooooooooooooo
 *                               ooooooooooooooooMoooooooooooMooooooooooooooooHo
 *                               ooMooooooooooooooMoooooooooooooooooooooooooooMoM
 *                              MooMoooooooooooooo.ooooooooooooooooooooooooooo:oM
 *                              MoooooooooooooooooooooooooooooooooooooooooooooooM
 *                              MoooMooooooooooooooMooooooooooooooooooooooooooooo.
 *                              MoooMooooooooooooooMoooooooooooooooooooooooooMooooM
 *                              MooooooooooooooooooMoooooooooooooooooooooooooMoooooM
 *                              MooooMoooooooooooooMoooooooooooooooooooooooooMoHooooM
 *                              ooooooMooooooooooooooooooooooooooooooooooooooooMoMoooM
 *                             MooooooooooooooooooooMooooooooooooooooooooooooooMoooooH:
 *                             MoooooooMooooooooooooMoooooooooooooooooooooooooooooHoooM
 *                             MooooooooMoooooooooooMoooooooooooooooooooooooooMoooMooooM
 *                             Moooooooooooooooooooooooooooooooooooooooooooooo.oooMooooo
 *                             MoooooooooooooooooooooooooooooooooooooooooooooMoooooooooM
 *                              MooooooooooooooooooooMoooooooooooooooooooooooooooooooooM
 *                               MooooooooooooooooooooMHooooooooooooooooooooMoooo:ooooo
 *                                MMooooooooooooooooooMoMHoooooooooooooooooooooooMooooo
 *                                 MMoooooooooooooooMMooo MMooooooooooooooooooooooooooM
 *                                 MMMoooooooooooooMooooo  oooooooooooooooooooooMooooo
 *                                 MooMMoooooooooMoooMMoM  ooooHooooooooooooooooMooooM
 *                                 MooooMooooooMooooMoooM  MoooooMoooooooooooooMooooo
 *                                 ooooooMMooooooooMooooM  MoooooooooMooooooooooooooM
 *                                 HooooooMoooooooMooooM    HoooooooHooMooooooooooooo
 *                                  oooMoooooooooHoooM         MoooooooooMoooooooooM
 *                                   HooooooooooooHM             MooooooooMMoooooooM
 *                                    MMMMMMMMMMMMMM                Moooooo:MooooHMM
 *                                     MMMMMMM: ...                  MMMMMMMMMMMMMM
 *                                    M............M                  MMMMMMMMM ....
 *                                    M.MM..........                  M.............M
 *                                 M ..............MM                 M..............
 *                              MMMMM............MMMM                 ..MMMMMMMM ....M
 *                            MMMMMMMMMMMMMMMMMMMMMMMM               MMMMMMMMMMMMM...M
 *                         .MMMMMMMMMMMMMMMMMMMMMMMMMM               MMMMMMMMMMMMMMMMMM
 *                         MMMMMMMMMMMMMMMMMMMMMMMMM                MMMMMMMMMMMMMMMMMMM
 *                         :MMMMMMMMMMMMMMMMMMH                     MMMMMMMMMMMMMMMMMMM
 *                            By EBEN Jérôme                        MMMMMMMMMMMMMMMMMM
 *                                                                  MMMMMMMMMMMMMMM
 *                                                                   HMMMMMM
 *
 *
 *
 *
 *   quu..__
 *          $$$b  `---.__
 *           "$$b        `--.                          ___.---uuudP
 *            `$$b           `.__.------.__     __.---'      $$$$"              .
 *              "$b          -'            `-.-'            $$$"              .'|
 *                ".                                       d$"             _.'  |
 *                  `.   /                              ..."             .'     |
 *                    `./                           ..::-'            _.'       |
 *                     /                         .:::-'            .-'         .'
 *                    :                          ::''\          _.'            |
 *                   .' .-.             .-.           `.      .'               |
 *                   : /'$$|           .@"$\           `.   .'              _.-'
 *                  .'|$u$$|          |$$,$$|           |  <            _.-'
 *                  | `:$$:'          :$$$$$:           `.  `.       .-'
 *                  :                  `"--'             |    `-.     \
 *                 :##.       ==             .###.       `.      `.    `\
 *                 |##:                      :###:        |        >     >
 *                 |#'     `..'`..'          `###'        x:      /     /
 *                  \                                   xXX|     /    ./
 *                   \                                xXXX'|    /   ./
 *                   /`-.                                  `.  /   /
 *                  :    `-  ...........,                   | /  .'
 *                  |         ``:::::::'       .            |<    `.
 *                  |             ```          |           x| \ `.:``.
 *                  |                         .'    /'   xXX|  `:`M`M':.
 *                  |    |                    ;    /:' xXXX'|  -'MMMMM:'
 *                  `.  .'                   :    /:'       |-'MMMM.-'
 *                   |  |                   .'   /'        .'MMM.-'
 *                   `'`'                   :  ,'          |MMM<
 *                     |                     `'            |tbap\
 *                      \                                  :MM.-'
 *                       \                 |              .''
 *                        \.               `.            /
 *                         /     .:::::::.. :           /
 *                        |     .:::::::::::`.         /
 *                        |   .:::------------\       /
 *                       /   .''               >::'  /
 *                       `',:                 :    .'
 *                                            `:.:'
 *
 *
 *                                                     ,----------------,
 *                 ,-----------------------,          ,"        ,"|
 *               ,"                      ,"|        ,"        ,"  |
 *              +-----------------------+  |      ,"        ,"    |
 *              |  .-----------------.  |  |     +---------+      |
 *              |  |                 |  |  |     | -==----'|      |
 *              |  |  I LOVE DOS!    |  |  |     |         |      |
 *              |  |  Bad command or |  |  |/----|`---=    |      |
 *              |  |  C:\>_          |  |  |   ,/|==== ooo |      ;
 *              |  |                 |  |  |  // |(((( [33]|    ,"
 *              |  `-----------------'  |," .;'| |((((     |  ,"
 *              +-----------------------+  ;;  | |         |,"
 *                 /_)______________(_/  //'   | +---------+
 *            ___________________________/___  `,
 *           /  oooooooooooooooo  .o.  oooo /,   \,"-----------
 *          / ==ooooooooooooooo==.o.  ooo= //   ,`\--{)B     ,"
 *         /_==__==========__==_ooo__ooo=_/'   /___________,"
 *
 *
 *
 *
 *                                                     __----~~~~~~~~~~~------___
 *                                           .  .   ~~//====......          __--~ ~~
 *                           -.            \_|//     |||\\  ~~~~~~::::... /~
 *                        ___-==_       _-~o~  \/    |||  \\            _/~~-
 *                __---~~~.==~||\=_    -_--~/_-~|-   |\\   \\        _/~
 *            _-~~     .=~    |  \\-_    '-~7  /-   /  ||    \      /
 *          .~       .~       |   \\ -_    /  /-   /   ||      \   /
 *         /  ____  /         |     \\ ~-_/  /|- _/   .||       \ /
 *         |~~    ~~|--~~~~--_ \     ~==-/   | \~--===~~        .\
 *                  '         ~-|      /|    |-~\~~       __--~~
 *                              |-~~-_/ |    |   ~\_   _-~            /\
 *                                   /  \     \__   \/~                \__
 *                               _--~ _/ | .-~~____--~-/                  ~~==.
 *                              ((->/~   '.|||' -_|    ~~-/ ,              . _||
 *                                         -_     ~\      ~~---l__i__i__i--~~_/
 *                                         _-~-__   ~)  \--______________--~~
 *                                       //.-~~~-~_--~- |-------~~~~~~~~
 *                                              //.-~~~--\
 *
 *                                                  ___====-_  _-====___
 *            _--^^^#####//      \\#####^^^--_
 *         _-^##########// (    ) \\##########^-_
 *        -############//  |\^^/|  \\############-
 *      _/############//   (@::@)   \\############\_
 *     /#############((     \\//     ))#############\
 *    -###############\\    (oo)    //###############-
 *   -#################\\  / VV \  //#################-
 *  -###################\\/      \//###################-
 * _#/|##########/\######(   /\   )######/\##########|\#_
 * |/ |#/\#/\#/\/  \#/\##\  |  |  /##/\#/  \/\#/\#/\#| \|
 * `  |/  V  V  `   V  \#\| |  | |/#/  V   '  V  V  \|  '
 *    `   `  `      `   / | |  | | \   '      '  '   '
 *                     (  | |  | |  )
 *                    __\ | |  | | /__
 *                   (vvv(VVV)(VVV)vvv)
 * @Description : 生产者
 * @Author : wangzifeng
 * @Createon : 2020/5/12.
 */

/**
 *                                _(\_/)
 *                              ,((((^`\
 *                             ((((  (6 \
 *                           ,((((( ,    \
 *       ,,,_              ,(((((  /"._  ,`,
 *      ((((\\ ,...       ,((((   /    `-.-'
 *      )))  ;'    `"'"'""((((   (
 *     (((  /            (((      \
 *      )) |                      |
 *     ((  |        .       '     |
 *     ))  \     _ '      `t   ,.')
 *     (   |   y;- -,-""'"-.\   \/
 *     )   / ./  ) /         `\  \
 *        |./   ( (           / /'
 *        ||     \\          //'|
 *        ||      \\       _//'||
 *        ||       ))     |_/  ||
 *        \_\     |_/          ||
 *        `'"                  \_\
 *                             `'"
 */


/***
 **************************************************************
 *                                                            *
 *   .=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-.       *
 *    |                     ______                     |      *
 *    |                  .-"      "-.                  |      *
 *    |                 /            \                 |      *
 *    |     _          |              |          _     |      *
 *    |    ( \         |,  .-.  .-.  ,|         / )    |      *
 *    |     > "=._     | )(__/  \__)( |     _.=" <     |      *
 *    |    (_/"=._"=._ |/     /\     \| _.="_.="\_)    |      *
 *    |           "=._"(_     ^^     _)"_.="           |      *
 *    |               "=\__|IIIIII|__/="               |      *
 *    |              _.="| \IIIIII/ |"=._              |      *
 *    |    _     _.="_.="\          /"=._"=._     _    |      *
 *    |   ( \_.="_.="     `--------`     "=._"=._/ )   |      *
 *    |    > _.="                            "=._ <    |      *
 *    |   (_/                                    \_)   |      *
 *    |                                                |      *
 *    '-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='      *
 *                                                            *
 *           LASCIATE OGNI SPERANZA, VOI CH'ENTRATE           *
 **************************************************************
 */



/***
 *                                         ,s555SB@@&
 *                                      :9H####@@@@@Xi
 *                                     1@@@@@@@@@@@@@@8
 *                                   ,8@@@@@@@@@B@@@@@@8
 *                                  :B@@@@X3hi8Bs;B@@@@@Ah,
 *             ,8i                  r@@@B:     1S ,M@@@@@@#8;
 *            1AB35.i:               X@@8 .   SGhr ,A@@@@@@@@S
 *            1@h31MX8                18Hhh3i .i3r ,A@@@@@@@@@5
 *            ;@&i,58r5                 rGSS:     :B@@@@@@@@@@A
 *             1#i  . 9i                 hX.  .: .5@@@@@@@@@@@1
 *              sG1,  ,G53s.              9#Xi;hS5 3B@@@@@@@B1
 *               .h8h.,A@@@MXSs,           #@H1:    3ssSSX@1
 *               s ,@@@@@@@@@@@@Xhi,       r#@@X1s9M8    .GA981
 *               ,. rS8H#@@@@@@@@@@#HG51;.  .h31i;9@r    .8@@@@BS;i;
 *                .19AXXXAB@@@@@@@@@@@@@@#MHXG893hrX#XGGXM@@@@@@@@@@MS
 *                s@@MM@@@hsX#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&,
 *              :GB@#3G@@Brs ,1GM@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@B,
 *            .hM@@@#@@#MX 51  r;iSGAM@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@8
 *          :3B@@@@@@@@@@@&9@h :Gs   .;sSXH@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@:
 *      s&HA#@@@@@@@@@@@@@@M89A;.8S.       ,r3@@@@@@@@@@@@@@@@@@@@@@@@@@@r
 *   ,13B@@@@@@@@@@@@@@@@@@@5 5B3 ;.         ;@@@@@@@@@@@@@@@@@@@@@@@@@@@i
 *  5#@@#&@@@@@@@@@@@@@@@@@@9  .39:          ;@@@@@@@@@@@@@@@@@@@@@@@@@@@;
 *  9@@@X:MM@@@@@@@@@@@@@@@#;    ;31.         H@@@@@@@@@@@@@@@@@@@@@@@@@@:
 *   SH#@B9.rM@@@@@@@@@@@@@B       :.         3@@@@@@@@@@@@@@@@@@@@@@@@@@5
 *     ,:.   9@@@@@@@@@@@#HB5                 .M@@@@@@@@@@@@@@@@@@@@@@@@@B
 *           ,ssirhSM@&1;i19911i,.             s@@@@@@@@@@@@@@@@@@@@@@@@@@S
 *              ,,,rHAri1h1rh&@#353Sh:          8@@@@@@@@@@@@@@@@@@@@@@@@@#:
 *            .A3hH@#5S553&@@#h   i:i9S          #@@@@@@@@@@@@@@@@@@@@@@@@@A.
 *
 *
 *    又看源码，看你妹妹呀！
 */



/***
 *             ,%%%%%%%%,
 *           ,%%/\%%%%/\%%
 *          ,%%%\c "" J/%%%
 * %.       %%%%/ o  o \%%%
 * `%%.     %%%%    _  |%%%
 *  `%%     `%%%%(__Y__)%%'
 *  //       ;%%%%`\-/%%%'
 * ((       /  `%%%%%%%'
 *  \\    .'          |
 *   \\  /       \  | |
 *    \\/         ) | |
 *     \         /_ | |__
 *     (___________))))))) 攻城湿
 *
 *        _       _
 * __   _(_)_   _(_) __ _ _ __
 * \ \ / / \ \ / / |/ _` |'_ \
 *  \ V /| |\ V /| | (_| | | | |
 *   \_/ |_| \_/ |_|\__,_|_| |_|
 */


/***
 *           _.._        ,------------.
 *        ,'      `.    ( We want you! )
 *       /  __) __` \    `-,----------'
 *      (  (`-`(-')  ) _.-'
 *      /)  \  = /  (
 *     /'    |--' .  \
 *    (  ,---|  `-.)__`
 *     )(  `-.,--'   _`-.
 *    '/,'          (  Uu",
 *     (_       ,    `/,-' )
 *     `.__,  : `-'/  /`--'
 *       |     `--'  |
 *       `   `-._   /
 *        \        (
 *        /\ .      \.  freebuf
 *       / |` \     ,-\
 *      /  \| .)   /   \
 *     ( ,'|\    ,'     :
 *     | \,`.`--"/      }
 *     `,'    \  |,'    /
 *    / "-._   `-/      |
 *    "-.   "-.,'|     ;
 *   /        _/["---'""]
 *  :        /  |"-     '
 *  '           |      /
 *              `      |
 */


/***
 *  .--,       .--,
 * ( (  \.---./  ) )
 *  '.__/o   o\__.'
 *     {=  ^  =}
 *      >  -  <
 *     /       \
 *    //       \\
 *   //|   .   |\\
 *   "'\       /'"_.-~^`'-.
 *      \  _  /--'         `
 *    ___)( )(___
 *   (((__) (__)))    高山仰止,景行行止.虽不能至,心向往之。
 */


/***
 *                   /88888888888888888888888888\
 *                   |88888888888888888888888888/
 *                    |~~____~~~~~~~~~"""""""""|
 *                   / \_________/"""""""""""""\
 *                  /  |              \         \
 *                 /   |  88    88     \         \
 *                /    |  88    88      \         \
 *               /    /                  \        |
 *              /     |   ________        \       |
 *              \     |   \______/        /       |
 *   /"\         \     \____________     /        |
 *   | |__________\_        |  |        /        /
 * /""""\           \_------'  '-------/       --
 * \____/,___________\                 -------/
 * ------*            |                    \
 *   ||               |                     \
 *   ||               |                 ^    \
 *   ||               |                | \    \
 *   ||               |                |  \    \
 *   ||               |                |   \    \
 *   \|              /                /"""\/    /
 *      -------------                |    |    /
 *      |\--_                        \____/___/
 *      |   |\-_                       |
 *      |   |   \_                     |
 *      |   |     \                    |
 *      |   |      \_                  |
 *      |   |        ----___           |
 *      |   |               \----------|
 *      /   |                     |     ----------""\
 * /"\--"--_|                     |               |  \
 * |_______/                      \______________/    )
 *                                               \___/
 */
@Component
public class Send {
    private final static String QUEUE_NAME = "hello";
    @Autowired
    RabbitTemplate rabbitTemplate;

    public  void send(){
        rabbitTemplate.convertAndSend("hello","sasdasdasd");
    }


















    public  void test() throws IOException {
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(true);

        /**
         * 队列名字
         * 如果我们声明一个持久队列(该队列将在服务器重启后继续存在)，则为true。
         *如果我们声明一个排他队列(仅限于此连接)，则为true。
         * 如果我们声明一个自动删除队列(服务器将在不再使用时删除它)，则为true
         * 队列的其他属性(构造参数)
         */
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        String message="Hello World!";
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
    }



    public  void test1() throws IOException, InterruptedException {
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(true);



        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
        Thread.currentThread().join();
    }

}
