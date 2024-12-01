
import FirebaseAnalytics
import FirebaseCore
// import FirebaseCrashlytics
import SwiftUI
import shared

class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate {

    lazy var applicationComponent: IosApplicationComponent = createApplicationComponent(
        appDelegate: self
    )

    func application(
        _: UIApplication,
        didFinishLaunchingWithOptions _: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        if !(FirebaseOptions.defaultOptions()?.apiKey?.isEmpty ?? true) {
            FirebaseApp.configure()
        }

        // Set the UNUserNotificationCenter delegate
//         UNUserNotificationCenter.current().delegate = self

        // Initiailize the AppInitializers
//         applicationComponent.initializers.initialize()

        return true
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification
    ) async -> UNNotificationPresentationOptions {
        return [.banner]
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse
    ) async {
//         let notification = response.notification
//         let deepLink = notification.request.content.userInfo["deeplink_uri"]

//         if deepLink != nil && deepLink is String {
//             // swiftlint:disable:next force_cast
//             applicationComponent.deepLinker.addDeeplink(string: deepLink as! String)
//         }
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            let uiComponent = createHomeUiControllerComponent(
                applicationComponent: delegate.applicationComponent
            )
            ContentView(component: uiComponent)
        }
    }
}

private func createApplicationComponent(
    appDelegate: AppDelegate
) -> IosApplicationComponent {
    return IosApplicationComponent.companion.create(

    )
}

private func createHomeUiControllerComponent(
    applicationComponent: IosApplicationComponent
) -> HomeUiControllerComponent {
    return HomeUiControllerComponent.companion.create(
        applicationComponent: applicationComponent
    )
}
