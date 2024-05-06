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

Rectangle {
    width: 1080
    height: 720

    color: Constants.backgroundColor

    View3D {
        id: view3D
        anchors.fill: parent
        anchors.leftMargin: 0
        anchors.rightMargin: 213
        anchors.topMargin: 76
        anchors.bottomMargin: 0

        environment: sceneEnvironment

        SceneEnvironment {
            id: sceneEnvironment
            antialiasingMode: SceneEnvironment.MSAA
            antialiasingQuality: SceneEnvironment.High
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
                eulerRotation.y: 45
                eulerRotation.x: 30
                materials: defaultMaterial
                source: "#Cube"
            }
        }

        Frame {
            id: frame
            x: 866
            y: -75
            width: 215
            height: 730
        }

        ProgressBar {
            id: progressBar
            x: 0
            y: -11
            width: 867
            height: 10
            value: 0.5
        }
    }

    Item {
        id: __materialLibrary__
        PrincipledMaterial {
            id: defaultMaterial
            objectName: "Default Material"
            baseColor: "#4aee45"
        }
    }

    Text {
        id: text1
        x: 0
        y: 0
        width: 867
        height: 65
        text: qsTr("CAR")
        font.pixelSize: 22
        horizontalAlignment: Text.AlignLeft
        rightPadding: 10
        bottomPadding: 20
        leftPadding: 10
        topPadding: 20
    }
}
