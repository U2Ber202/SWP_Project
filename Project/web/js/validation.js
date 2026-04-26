/**
 * Centralized Validation Library
 */

const Validation = {
    // Regex Patterns
    emailPattern: /^[A-Za-z0-9+_.-]+@(.+)$/,
    phonePattern: /^0[35789]\d{8}$/,
    badWords: [
        "dm", "vcl", "cl", "dcm", "cho", "ngu", "cac", "lon", "buoi", "du", "dit", "me", "ba", "ma",
        "khon", "nan", "mat", "day", "oc", "cho", "do", "dien", "khung", "di", "diem",
        "fuck", "shit", "bitch", "ass", "bastard", "dick", "pussy", "fucker", "shitty", "hell", "damn"
    ],

    /**
     * Check if content contains bad words
     */
    containsBadWords: function(content) {
        if (!content) return false;
        const lowerContent = content.toLowerCase();
        return this.badWords.some(word => {
            const regex = new RegExp(`\\b${word}\\b`, 'i');
            return regex.test(lowerContent);
        });
    },

    /**
     * Validate Email
     */
    isValidEmail: function(email) {
        return this.emailPattern.test(email.trim());
    },

    /**
     * Validate Phone
     */
    isValidPhone: function(phone) {
        return this.phonePattern.test(phone.trim());
    },

    /**
     * Validate Image Size (Max 2MB)
     */
    isValidImageSize: function(file) {
        const maxSize = 2 * 1024 * 1024; // 2MB
        return file.size <= maxSize;
    },

    /**
     * Scan image for NSFW content using NSFWJS
     * Returns a promise that resolves to true if safe, false if unsafe
     */
    isSafeImage: async function(imgElement) {
        if (typeof nsfwjs === 'undefined') {
            console.error('NSFWJS library not loaded');
            return true; // Default to true if lib fails to load
        }

        try {
            const model = await nsfwjs.load();
            const predictions = await model.classify(imgElement);
            
            console.log('NSFW Predictions:', predictions);
            
            // Check top predictions
            const unsafeLabels = ['Porn', 'Hentai', 'Sexy'];
            const threshold = 0.5; // Sensitivity threshold
            
            for (let pred of predictions) {
                if (unsafeLabels.includes(pred.className) && pred.probability > threshold) {
                    return false; // Unsafe content detected
                }
            }
            return true; // Safe
        } catch (error) {
            console.error('NSFW Scanning error:', error);
            return true; // Assume safe if scanning fails
        }
    }
};

// Global function to attach to file inputs for instant feedback
async function handleImageUpload(inputElement, previewElementId, submitBtnId) {
    const file = inputElement.files[0];
    const errorMsgId = inputElement.id + '-error';
    let errorMsg = document.getElementById(errorMsgId);
    
    if (!errorMsg) {
        errorMsg = document.createElement('div');
        errorMsg.id = errorMsgId;
        errorMsg.style.color = 'red';
        errorMsg.style.fontSize = '0.8em';
        errorMsg.style.marginTop = '5px';
        inputElement.parentNode.appendChild(errorMsg);
    }

    const submitBtn = document.getElementById(submitBtnId);
    if (submitBtn) submitBtn.disabled = true;

    if (!file) {
        errorMsg.innerText = '';
        if (submitBtn) submitBtn.disabled = false;
        return;
    }

    // 1. Check size
    if (!Validation.isValidImageSize(file)) {
        errorMsg.innerText = 'Ảnh quá lớn! Vui lòng chọn ảnh dưới 2MB.';
        inputElement.value = '';
        if (submitBtn) submitBtn.disabled = false;
        return;
    }

    errorMsg.innerText = 'Đang quét nội dung ảnh...';
    errorMsg.style.color = 'orange';

    // 2. Check NSFW (if nsfwjs is available)
    const reader = new FileReader();
    reader.onload = async function(e) {
        const img = new Image();
        img.src = e.target.result;
        img.onload = async function() {
            // Preview
            if (previewElementId) {
                const preview = document.getElementById(previewElementId);
                if (preview) preview.src = img.src;
            }

            const isSafe = await Validation.isSafeImage(img);
            if (!isSafe) {
                errorMsg.innerText = 'Ảnh bị phát hiện chứa nội dung không phù hợp!';
                errorMsg.style.color = 'red';
                inputElement.value = '';
                if (previewElementId) document.getElementById(previewElementId).src = '';
            } else {
                errorMsg.innerText = 'Ảnh hợp lệ.';
                errorMsg.style.color = 'green';
            }
            if (submitBtn) submitBtn.disabled = false;
        };
    };
    reader.readAsDataURL(file);
}
