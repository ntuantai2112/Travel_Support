import React from 'react';
import '../style/post.scss';
import '../style/UniquePlace.scss';
import '../style/exploreVN.scss';
import '../style/style.scss';
import UniquePlace from '../component/share/UniquePlace';
import ExploreVN from '../component/share/ExploreVN';
import Header from '../component/share/Header';
import Footer from '../component/share/Footer';
import Post from '../component/post/Post'

export default function PostPage() {
    return (
        <>

            <div className="wrapper__post-page container">
                <Header />
                <div className="row row__post-page">
                    <div className="col-12 col__post-page">
                        <Post />
                    </div>
                </div>
                <div className="row row__unique">
                    <div className="col-12 col__unique">
                        <UniquePlace />
                    </div>
                </div>
                <div className="row row__explore">
                    <div className="col-12 col__explore">
                        <ExploreVN />
                    </div>
                </div>
            </div>
            <Footer />
        </>
    )
}