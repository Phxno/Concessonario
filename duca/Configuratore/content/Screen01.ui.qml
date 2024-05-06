

/*
This is a UI file (.ui.qml) that is intended to be edited in Qt Design Studio only.
It is supposed to be strictly declarative and only uses a subset of QML. If you edit
this file manually, you might introduce QML code that is not supported by Qt Design Studio.
Check out https://doc.qt.io/qtcreator/creator-quick-ui-forms.html for details on .ui.qml files.
*/
import QtQuick 6.5
import QtQuick.Controls 6.5
import QtQuick3D 6.5
import QtQuick3D.Effects 6.5
import Configuratore
import QtQuick3D.Helpers
import Quick3DAssets.Uploads_files_3262252_r8

Rectangle {
    width: 1080
    height: 720

    color: Constants.backgroundColor

    ProgressBar {
        id: progressBar
        x: 0
        y: 79
        width: 860
        height: 23
        value: 0.5
    }

    Text {
        id: text1
        x: 0
        y: 0
        width: 860
        height: 73
        text: qsTr("CAR")
        font.pixelSize: 22
        verticalAlignment: Text.AlignVCenter
        rightPadding: 20
        leftPadding: 20
        bottomPadding: 0
        topPadding: 0
    }

    View3D {
        id: view3D
        x: 0
        y: 108
        width: 860
        height: 612
        environment: sceneEnvironment
        SceneEnvironment {
            id: sceneEnvironment
            antialiasingQuality: SceneEnvironment.High
            antialiasingMode: SceneEnvironment.MSAA
        }

        Node {
            id: scene
            DirectionalLight {
                id: directionalLight
            }

            PerspectiveCamera {
                id: sceneCamera
                z: 350
            }

            Model {
                id: cubeModel
                source: "#Cube"
                eulerRotation.y: 45
                eulerRotation.x: 30
            }
        }

        Column {
            id: column
            x: 860
            y: -106
            width: 220
            height: 718
        }
    }
}
