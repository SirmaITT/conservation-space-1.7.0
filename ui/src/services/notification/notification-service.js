import {Injectable} from 'app/app';
import _ from 'lodash';
import toastr from 'CodeSeven/toastr';
import 'CodeSeven/toastr/toastr.css!';

//TOO uppser case for the constants?
const titles = {
  success: 'Done',
  info: 'Info',
  warning: 'Warning',
  error: 'Error'
};
export const DEFAULT_POSITION = 'toast-bottom-right';
export const TOP_POSITION = 'toast-top-right';

export const ERROR_TIMEOUT = 30000;

/**
 * Service that provides methods to rise notification popups.
 *
 * @use
 * <code>
 *  // displays popup with default title and no message
 *  notificationService.error();
 *  // displays message with 'Server error' title and no message
 *  notificationService.error('Server error');
 *  // displays message with 'Server error' title and no message
 *  notificationService.error({ title: 'Server error' });
 *  // displays message with 'Server error' title and 'server unavailable' message
 *  notificationService.error({ title: 'Server error', message: 'server unavailable' });
 *  // displays message with 'Server error' title and 'server unavailable' message and additional options
 *  notificationService.error({ title: 'Server error', message: 'server unavailable', opts: {} });
 * </code>
 *
 * Options:
 * - closeButton - when true the notification will only be closed by clicking on the button or after specific time
 * after hovering the notification popup. Default: false
 * - hideOnHover - when true the notification won't automatically hide after hovering. Default: true
 *
 * @see https://github.com/CodeSeven/toastr
 *
 * @author svelikov
 */
@Injectable()
export class NotificationService {

  constructor() {
    toastr.options.progressBar = true;
    toastr.options.newestOnTop = false;
    toastr.options.preventDuplicates = true;
    // How long the toast will display without user interaction.
    // Set timeOut=0 for sticky notification until user dismisses it.
    toastr.options.timeOut = 5000;
    // How long the toast will display after a user hovers over it
    toastr.options.extendedTimeOut = 2000;
    // one of: toast-top-left, toast-top-right, toast-bottom-left, toast-bottom-right
    // toast-top-full-width, toast-bottom-full-width, toast-top-center, toast-bottom-center
    toastr.options.positionClass = DEFAULT_POSITION;
    toastr.options.closeButton = true;
    toastr.options.hideOnHover = true;
  }

  success(opts) {
    this.notify('success', opts);
  }

  info(opts) {
    this.notify('info', opts);
  }

  warning(opts) {
    this.notify('warning', opts);
  }

  error(opts) {
    let defaultOpts = {
      opts: {
        timeOut: ERROR_TIMEOUT
      }
    };
    _.defaultsDeep(opts, defaultOpts);
    this.notify('error', opts);
  }

  /**
   * Remove current toasts using animation.
   */
  clear() {
    toastr.clear();
  }

  /**
   * Immediately remove current toasts without using animation.
   */
  remove() {
    toastr.remove();
  }

  /**
   * Private method that validates and builds options for the underlying plugin
   * before invocation.
   *
   * @param type
   *          Is the notification type. Actually this is the service method that
   *          was invoked.
   * @param opts
   *          The configuration options to be used.
   */
  notify(type, opts) {
    var _opts = opts || {};
    var title = this.getTitle(type, opts);
    var message = this.getMessage(opts);
    var toastrConfig = _opts.opts || {};
    // in case the closeButton=true option is provided, we set the timeOut=0
    // to make the popup permanent until user closes it
    if (toastrConfig.closeButton && toastrConfig.closeButton === true) {
      toastrConfig.timeOut = 0;
    }

    if (toastrConfig.hideOnHover === false) {
      toastrConfig.extendedTimeOut = 0;
    }

    if (message.length > 500 && !toastrConfig.positionClass) {
      toastrConfig.positionClass = TOP_POSITION;
    }

    toastr[type](message, title, toastrConfig);
  }

  /**
   * Calculates the notification message title and sets it to the options. Allows custom
   * titles to be passed and used instead of default once.
   *
   * @param type
   *          Is the notification type. Actually this is the service method that
   *          was invoked.
   * @param opts
   *          The configuration options to be used.
   */
  getTitle(type, opts) {
    return opts.title || titles[type];
  }

  /**
   * Get proper notification message. Allow a simple message to be passed and displayed without
   * any other options.
   *
   * @param opts
   *          The configuration options to be used.
   */
  getMessage(opts) {
    var message;
    if (typeof opts === 'string') {
      message = opts;
    } else {
      message = (opts && opts.message) ? opts.message : '';
    }
    return message;
  }

}